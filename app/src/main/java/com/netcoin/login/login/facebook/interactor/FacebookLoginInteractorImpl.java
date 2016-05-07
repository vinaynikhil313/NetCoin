package com.netcoin.login.login.facebook.interactor;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.netcoin.application.NetCoinApp;
import com.netcoin.login.login.facebook.presenter.OnFacebookLoginFinishedListener;
import com.netcoin.user.User;
import com.netcoin.utils.Constants;
import com.netcoin.utils.RestApiHelper;
import com.netcoin.utils.UpdateFirebaseLogin;
import com.netcoin.utils.Utilities;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class FacebookLoginInteractorImpl implements
		FacebookLoginInteractor, Firebase.AuthResultHandler, ValueEventListener {

	private Firebase firebase = NetCoinApp.getFirebaseInstance();

	private final String TAG = Utilities.getTag(this);

	private OnFacebookLoginFinishedListener listener;

	public FacebookLoginInteractorImpl(OnFacebookLoginFinishedListener listener) {
		this.listener = listener;
	}

	@Override
	public void generateOTP(String phoneNo) {
		RequestParams requestParams = new RequestParams();
		requestParams.add("phoneNo", phoneNo);
		RestApiHelper.get(Constants.VALIDATE_URL, requestParams, new JsonHttpResponseHandler() {
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
	public void requestData() {
		//Log.i(TAG + " inside requestData ", AccessToken.getCurrentAccessToken ().getToken ());
		//this.listener = listener;
		GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {

						JSONObject json = response.getJSONObject();
						Log.i("JSON ", json.toString());

						firebase.authWithOAuthToken(Constants.PROVIDER_FACEBOOK,
								AccessToken.getCurrentAccessToken().getToken(), FacebookLoginInteractorImpl.this);


					}
				}
		);

		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,name,link,email,picture");
		request.setParameters(parameters);
		request.executeAsync();
	}

	@Override
	public void onAuthenticated(AuthData authData) {
		UpdateFirebaseLogin.updateFirebase(authData);

		firebase.child("users").child(authData.getUid()).addListenerForSingleValueEvent(this);
	}

	@Override
	public void onAuthenticationError(FirebaseError firebaseError) {
		listener.onFirebaseLoginFailure();
	}

	@Override
	public void onCancelled(FirebaseError firebaseError) {

	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		User user = dataSnapshot.getValue(User.class);
		Log.i(TAG, "UID = " + user.getUid());
		listener.onFirebaseLoginSuccess(user);
	}
}
