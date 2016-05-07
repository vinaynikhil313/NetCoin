package com.netcoin.login.login.facebook.presenter;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.netcoin.login.login.facebook.interactor.FacebookLoginInteractor;
import com.netcoin.login.login.facebook.interactor.FacebookLoginInteractorImpl;
import com.netcoin.login.login.facebook.view.FacebookLoginFragmentView;
import com.netcoin.user.User;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class FacebookLoginPresenterImpl implements FacebookLoginPresenter {

	private FacebookLoginFragmentView view;
	private FacebookLoginInteractor interactor;

	public static final int MODE_PHONE_NO = 0;
	public static final int MODE_OTP = 1;

	private String otp;


	private static final String TAG = FacebookLoginPresenterImpl.class.getSimpleName();

	public FacebookLoginPresenterImpl(FacebookLoginFragmentView view) {

		this.view = view;
		interactor = new FacebookLoginInteractorImpl(this);
		Log.i(TAG, "FacebookPresenter created");

	}

	@Override
	public void onAlertDialogTextEntered(int mode, String text) {
		if(mode == MODE_PHONE_NO) {
			view.showProgressDialog("Generating OTP");
			interactor.generateOTP(text);
		} else if(mode == MODE_OTP) {
			view.showProgressDialog("Verifying OTP");
			if(otp.equals(text)) {
				view.showToastMessage("OTP Verified. Registering User");
				interactor.requestData();
			} else {
				view.hideProgressDialog();
				view.showToastMessage("Incorrect OTP Entered. Please try again");
				view.showAlertDialogBox(MODE_OTP, "Enter the OTP received");
			}
		}
	}

	@Override
	public void onOTPReceived(String OTP) {
		this.otp = OTP;
		view.hideProgressDialog();
		view.showAlertDialogBox(MODE_OTP, "Enter the OTP received");
	}

	@Override
	public void onFirebaseLoginFailure() {
		view.hideProgressDialog();
		view.onError();
	}

	@Override
	public void onFirebaseLoginSuccess(User user) {
		Log.i(TAG, "Firebase Facebook Login successful");
		view.writeToSharedPreferences(user);
		view.hideProgressDialog();
		view.openMainPage();
	}

	@Override
	public void onCancel() {

	}

	@Override
	public void onError(FacebookException error) {

	}

	@Override
	public void onSuccess(LoginResult loginResult) {
		view.showAlertDialogBox(MODE_PHONE_NO, "Enter Phone Number");
		AccessToken accessToken = loginResult.getAccessToken();
		Log.i(TAG, accessToken.getToken());
		AccessToken.setCurrentAccessToken(accessToken);
		Log.i(TAG, "Facebook Login Successful");
		//interactor.requestData(this);

	}
}
