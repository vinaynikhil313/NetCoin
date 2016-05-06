package com.netcoin.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.netcoin.utils.Constants;
import com.netcoin.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 20-02-2016.
 */
public class NetCoinApp extends Application {

	private static Firebase mFirebase;

	private final String TAG = Utilities.getTag(this);

	@Override
	public void onCreate() {
		super.onCreate();

		FacebookSdk.sdkInitialize(getApplicationContext());

		Firebase.setAndroidContext(this);
		Firebase.getDefaultConfig().setPersistenceEnabled(true);

		mFirebase = new Firebase(Constants.FIREBASE_REF);

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static Firebase getFirebaseInstance() {
		return mFirebase;
	}

}
