package com.netcoin.start.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.netcoin.R;
import com.netcoin.login.login.LoginActivity;
import com.netcoin.main.view.MainActivity;
import com.netcoin.start.presenter.StartPagePresenter;
import com.netcoin.start.presenter.StartPagePreviousLoginChecker;
import com.netcoin.user.User;
import com.netcoin.utils.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Vinay Nikhil Pabba on 16-01-2016. Simple Start screen. Waits for 5 seconds for the Facebook SDK to
 * initialize. Also validates the access token if already present and logs the user in directly without going to the
 * Login screen by using AuthenticateUser class
 */
public class StartPage extends Activity implements StartPageView {

	private boolean openLoginPageFlag = true;
	private ProgressBar progressBar;

	private StartPagePresenter presenter;

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	private static final String TAG = StartPage.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_activity_main);

		displayHashKey();

		presenter = new StartPagePreviousLoginChecker(this);

		FacebookSdk.sdkInitialize(getApplicationContext(), presenter);

		sharedPreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		if(! isNetworkAvailable()) {
			//progressBar.setVisibility(View.GONE);
			Log.i(TAG, "No Internet");
			Toast.makeText(StartPage.this, "Internet not available..." +
					"\nPlease check your internet and try again", Toast.LENGTH_LONG).show();
			//finish();
		}

		//isLocationEnabled();

		checkPreviousPasswordLogin();

	}

	void checkPreviousPasswordLogin() {
		Log.i(TAG, "Checking previous password login");
		String userJson = sharedPreferences.getString("user", "");
		Log.i(TAG, "User is " + userJson);
		if(! userJson.equals("") && ! userJson.isEmpty()) {
			Log.i(TAG, "UserJson = " + userJson);
			Log.i(TAG, "Provider is password");
			Gson gson = new Gson();
			//String userJson = sharedPreferences.getString ("user", "");
			User user = gson.fromJson(userJson, User.class);
			if(user == null)
				return;
			String token = user.getAccessToken(); //sharedPreferences.getString ("accessToken", "");
			Log.i(TAG + " Token ", token);
			if(user.getProvider().equals(Constants.PROVIDER_PASSWORD) && ! token.equals("")) {
				presenter.loginWithPassword(token);
				disableLoginPage();
			}

		}
	}

	@Override
	public void writeToSharedPreferences(User user) {

		Log.i(TAG, user.getAccessToken());

		Gson gson = new Gson();
		String userJson = gson.toJson(user);
		editor = sharedPreferences.edit();
		editor.putString("user", userJson);
		editor.apply();
		Log.i("Login UID", "The user is - " + sharedPreferences.getString("user", ""));

	}

	@Override
	public void showMessage(String message) {
		Toast.makeText(StartPage.this, message, Toast.LENGTH_SHORT).show();
	}

	private void displayHashKey() {

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.example.benjaminlize.loginapp",
					PackageManager.GET_SIGNATURES);
			for(Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch(PackageManager.NameNotFoundException | NoSuchAlgorithmException  e) {
			Log.i(TAG, e.getLocalizedMessage());
		}

	}


	@Override
	protected void onResume() {
		super.onResume();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if(openLoginPageFlag) {
					openLoginPage();
				}
			}

		}, 5000);
	}

	@Override
	public void openLoginPage() {
		Log.i(TAG, "Opening Login Page");
		startActivity(new Intent(StartPage.this, LoginActivity.class));
		finish();

	}

	@Override
	public void openMainPage() {
		Log.i(TAG, "Opening Main Page");
		startActivity(new Intent(StartPage.this, MainActivity.class));
		finish();
	}

	@Override
	public void disableLoginPage() {
		openLoginPageFlag = false;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private boolean isLocationEnabled() {

		AlertDialog dialog;
		LocationManager lm;
		boolean gps_enabled = false, network_enabled = false;
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch(Exception ex) {
			Log.i(TAG, ex.getLocalizedMessage());
		}
		try {
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch(Exception ex) {
			Log.i(TAG, ex.getLocalizedMessage());
		}

		if(! gps_enabled && ! network_enabled) {
			dialog = new AlertDialog.Builder(this)
					.setMessage("Do you want to turn on Location Services?")
					.setPositiveButton("Yes", new DialogInterface
							.OnClickListener() {

						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(myIntent);
							//get gps
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							paramDialogInterface.dismiss();
						}
					})
					.setCancelable(true)
					.create();
			dialog.show();

		} else
			return true;

		return false;
	}

}
