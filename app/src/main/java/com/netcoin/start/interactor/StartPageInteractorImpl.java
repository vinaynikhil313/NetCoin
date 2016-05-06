package com.netcoin.start.interactor;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.netcoin.application.NetCoinApp;
import com.netcoin.start.presenter.OnTokenLoginFinishedListener;
import com.netcoin.user.User;
import com.netcoin.utils.Constants;
import com.netcoin.utils.UpdateFirebaseLogin;
import com.netcoin.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public class StartPageInteractorImpl implements StartPageInteractor, Firebase.AuthResultHandler, ValueEventListener {

	private Firebase firebase = NetCoinApp.getFirebaseInstance();

	OnTokenLoginFinishedListener listener;

	String provider;

	String TAG = Utilities.getTag(this);

	@Override
	public void loginWithToken(String provider, String accessToken, OnTokenLoginFinishedListener listener) {
		this.listener = listener;
		this.provider = provider;
		if(provider.equals(Constants.PROVIDER_FACEBOOK))
			firebase.authWithOAuthToken(provider, accessToken, this);
		else
			firebase.authWithCustomToken(accessToken, this);
	}

	@Override
	public void onAuthenticated(AuthData authData) {
		Log.i(TAG, "Login Successful");
		UpdateFirebaseLogin.updateFirebase(authData);
		//listener.onLoginSuccessful(provider, authData.getUid(), authData.getToken());
		firebase.child ("users").child (authData.getUid ()).addListenerForSingleValueEvent (this);
	}

	@Override
	public void onAuthenticationError(FirebaseError firebaseError) {
		Log.e(TAG, firebaseError.getMessage());
		listener.onLoginUnsuccessful();
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		User user = dataSnapshot.getValue (User.class);
		Log.i("Start Page Interactor", "UID + " + user.getUid ());
		listener.onLoginSuccessful(user);
	}

	@Override
	public void onCancelled(FirebaseError firebaseError) {

	}
}
