package com.netcoin.login.register.view;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.netcoin.R;
import com.netcoin.login.register.presenter.RegisterPresenter;
import com.netcoin.login.register.presenter.RegisterPresenterImpl;
import com.netcoin.main.view.MainActivity;
import com.netcoin.user.User;
import com.netcoin.utils.Constants;
import com.netcoin.utils.Utilities;


/**
 * Created by Vinay Nikhil Pabba on 15-01-2016. Main Register Screen Contains the DetailsFragment and a button to create
 * user using the AuthenticateUser Class.
 */
public class RegisterActivity extends AppCompatActivity implements RegisterActivityView {

	private Fragment details;

	private RegisterPresenter presenter;

	private EditText email;
	private EditText password;
	private EditText phoneNo;

	private ProgressDialog progressDialog;

	private Button register;

	private final String TAG = Utilities.getTag(this);

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_register_main);

		presenter = new RegisterPresenterImpl(this);

		sharedPreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);

		details = getFragmentManager().findFragmentById(R.id.registerDetailsFragment);

		email = (EditText) details.getView().findViewById(R.id.emailText);
		password = (EditText) details.getView().findViewById(R.id.passwordText);
		phoneNo = (EditText) findViewById(R.id.phoneNumber);

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);

		register = (Button) findViewById(R.id.registerButton);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.createUser(email.getText().toString(), password.getText().toString(), phoneNo.getText().toString());
			}
		});

	}

	@Override
	public void showOTPDialogBox() {
		final EditText otpEditText = new EditText(this);
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("Enter the OTP received")
				.setView(otpEditText)
				.setPositiveButton("Validate", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String otp = otpEditText.getText().toString();
						presenter.onOTPEntered(otp);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setCancelable(false)
				.create();
		alertDialog.show();

	}

	@Override
	public void hideProgressBar() {
		progressDialog.hide();
	}

	@Override
	public void showProgressBar(String message) {
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	@Override
	public void openHomePage() {
		Toast.makeText(RegisterActivity.this, "Welcome to Net Coin", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(RegisterActivity.this, MainActivity.class));
		finish();
	}

	@Override
	public void registrationError(String message) {
		Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void writeToSharedPreferences(User user) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(user);
		Log.i(TAG, jsonString);
		editor = sharedPreferences.edit();
		editor.putString("user", jsonString);
		editor.apply();
	}

	@Override
	public void onStop() {
		super.onStop();
		if(progressDialog != null)
			progressDialog.dismiss();
	}

}
