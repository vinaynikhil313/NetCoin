package com.netcoin.login.register.view;

import com.netcoin.user.User;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface RegisterActivityView {

	void showOTPDialogBox();

	void openHomePage();

	void registrationError(String message);

	void showProgressBar(String message);

	void hideProgressBar();

	void writeToSharedPreferences(User user);

}
