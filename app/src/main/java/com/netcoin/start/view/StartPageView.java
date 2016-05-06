package com.netcoin.start.view;

import com.netcoin.user.User;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface StartPageView {

	void writeToSharedPreferences(User user);

	void showMessage(String message);

	void openMainPage();

	void openLoginPage();

	void disableLoginPage();

}
