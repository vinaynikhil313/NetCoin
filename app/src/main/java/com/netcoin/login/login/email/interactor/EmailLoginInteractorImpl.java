package com.netcoin.login.login.email.interactor;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.netcoin.application.NetCoinApp;
import com.netcoin.login.login.email.presenter.OnEmailLoginFinishedListener;
import com.netcoin.user.User;
import com.netcoin.utils.UpdateFirebaseLogin;

/**
 * Created by Vinay Nikhil Pabba on 21-01-2016.
 */
public class EmailLoginInteractorImpl implements
		EmailLoginInteractor, Firebase.AuthResultHandler, ValueEventListener {

	Firebase firebase = NetCoinApp.getFirebaseInstance();

	private final String TAG = EmailLoginInteractorImpl.class.getSimpleName();

	private OnEmailLoginFinishedListener listener;

	@Override
	public void validatePhoneNo(String phoneNo) {

	}

	@Override
	public void authenticateWithEmail(String email, String password, OnEmailLoginFinishedListener listener) {
		this.listener = listener;
		Log.i(TAG, "Login with email called");
		firebase.authWithPassword(email, password, EmailLoginInteractorImpl.this);

	}


	@Override
	public void onAuthenticated(AuthData authData) {

		Log.i(TAG, "Login with email successful");

		UpdateFirebaseLogin.updateFirebase(authData);

		firebase.child("users").child(authData.getUid()).addListenerForSingleValueEvent(this);

	}

	@Override
	public void onAuthenticationError(FirebaseError firebaseError) {

		switch(firebaseError.getCode()) {

			case FirebaseError.USER_DOES_NOT_EXIST:
				listener.onEmailError();
				break;

			case FirebaseError.INVALID_PASSWORD:
				listener.onPasswordError();
				break;

			case FirebaseError.INVALID_EMAIL:
				listener.onEmailError();
				break;

		}

	}

	@Override
	public void onCancelled(FirebaseError firebaseError) {

	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		User user = dataSnapshot.getValue(User.class);
		Log.i("EMAIL INTERACTOR", "User = " + user);
		listener.onSuccess(user);
	}
}
