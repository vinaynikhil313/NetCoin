package com.netcoin.login.register.interactor;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.netcoin.application.NetCoinApp;
import com.netcoin.login.register.presenter.OnRegisterFinishedListener;
import com.netcoin.user.User;
import com.netcoin.utils.Constants;
import com.netcoin.utils.RestApiHelper;
import com.netcoin.utils.UpdateFirebaseLogin;
import com.netcoin.utils.Utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class RegisterInteractorImpl implements RegisterInteractor,
		Firebase.ValueResultHandler<Map<String, Object>>,
		Firebase.AuthResultHandler,
		ValueEventListener{

	String email;
	String password;
	String phoneNo;

	private final String TAG = Utilities.getTag(this);

	public RegisterInteractorImpl(OnRegisterFinishedListener listener) {
		this.listener = listener;
	}

	OnRegisterFinishedListener listener;

	private Firebase firebase = NetCoinApp.getFirebaseInstance();

	@Override
	public void generateOTP(String phoneNo) {
		RequestParams requestParams = new RequestParams();
		requestParams.add("phoneNo", phoneNo);
		RestApiHelper.get(Constants.VALIDATE_URL, requestParams, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.i(TAG, response.toString());
				listener.onOTPReceived("123abc");
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	public void registerUser(String email, String password, String phoneNo) {
		firebase.createUser(email, password, this);
		this.email = email;
		this.password = password;
		this.phoneNo = phoneNo;
	}

	@Override
	public void onError(FirebaseError firebaseError) {
		switch(firebaseError.getCode()) {
			case FirebaseError.EMAIL_TAKEN:
				listener.onFailure(FirebaseError.EMAIL_TAKEN);
				break;
		}
	}

	@Override
	public void onSuccess(Map<String, Object> stringObjectMap) {
		Log.i("Authenticate Create", stringObjectMap.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("displayName", "user");
		map.put("email", email);
		map.put("provider", "password");
		map.put("phoneNo", phoneNo);
		firebase.child("users").child((String) stringObjectMap.get("uid")).setValue(map);
		firebase.authWithPassword(email, password, this);
	}

	@Override
	public void onAuthenticated(AuthData authData) {
		//listener.onSuccess(authData.getUid(), authData.getToken());

		UpdateFirebaseLogin.updateFirebase(authData);

		firebase.child ("users").child (authData.getUid ()).addListenerForSingleValueEvent (this);

	}

	@Override
	public void onAuthenticationError(FirebaseError firebaseError) {
		listener.onFailure(firebaseError.getCode());
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		User user = dataSnapshot.getValue (User.class);
		Log.i(TAG, "UID + " + user.getUid ());
		listener.onSuccess (user);
	}

	@Override
	public void onCancelled(FirebaseError firebaseError) {

	}
}
