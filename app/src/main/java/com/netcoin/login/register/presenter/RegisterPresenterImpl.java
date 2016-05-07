package com.netcoin.login.register.presenter;

import android.util.Log;

import com.firebase.client.FirebaseError;
import com.netcoin.login.register.interactor.RegisterInteractor;
import com.netcoin.login.register.interactor.RegisterInteractorImpl;
import com.netcoin.login.register.view.RegisterActivity;
import com.netcoin.login.register.view.RegisterActivityView;
import com.netcoin.user.User;
import com.netcoin.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class RegisterPresenterImpl implements RegisterPresenter, OnRegisterFinishedListener {

	private String TAG = Utilities.getTag(this);
	private RegisterInteractor interactor;
	private RegisterActivityView view;
	private String email, password, phoneNo, otp;

	public RegisterPresenterImpl(RegisterActivity view) {

		this.view = view;
		interactor = new RegisterInteractorImpl(this);

	}

	@Override
	public void createUser(String email, String password, String phoneNo) {
		view.showProgressBar("Generating OTP");
		if(validate(email, password, phoneNo)) {
			this.email = email;
			this.password = password;
			this.phoneNo = phoneNo;
			interactor.generateOTP(phoneNo);
			///interactor.registerUser(email, password, phoneNo);
		}
	}

	@Override
	public void onOTPReceived(String OTP) {
		this.otp = OTP;
		view.hideProgressBar();
		view.showOTPDialogBox();
	}

	@Override
	public void onOTPEntered(String OTP) {
		view.showProgressBar("Verifying OTP");
		if(otp.equals(OTP)) {
			view.showProgressBar("OTP Verified. Registering User");
			interactor.registerUser(email, password, phoneNo);
		}
		else {
			view.hideProgressBar();
			view.registrationError("Incorrect OTP Entered. Please try again");
			view.showOTPDialogBox();
		}
	}

	public boolean validate(String email, String password, String phoneNo) {

		boolean valid = true;

		if(email.isEmpty() || ! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			view.hideProgressBar();
			view.registrationError("Enter a valid email address");
			valid = false;
		}

		if(password.isEmpty() || password.length() < 5 || password.length() > 20) {
			view.hideProgressBar();
			view.registrationError("Between 4 and 20 alphanumeric characters");
			valid = false;
		}

		if(phoneNo.isEmpty() || phoneNo.length() != 10){
			view.hideProgressBar();
			view.registrationError("Incorrect phone number");
			valid = false;
		}

		return valid;
	}

	@Override
	public void onFailure(int errorCode) {
		view.hideProgressBar();
		switch(errorCode) {
			case FirebaseError.EMAIL_TAKEN:
				view.hideProgressBar();
				view.registrationError("Email already taken\nTry logging in");
				break;

			case FirebaseError.INVALID_EMAIL:
				view.hideProgressBar();
				view.registrationError("Invalid email. Please try again");
				break;

			default:
				view.hideProgressBar();
				view.registrationError("Error in creating user - " + errorCode);
		}
	}

	@Override
	public void onSuccess(User user) {
		Log.i(TAG, "On Success " + user.getEmail());
		view.hideProgressBar();
		view.writeToSharedPreferences(user);
		view.openHomePage();
	}
}
