package com.netcoin.login.login.facebook.view;

import com.netcoin.user.User;

/**
 * Created by Vinay Nikhil Pabba on 22-01-2016.
 */
public interface FacebookLoginFragmentView {

	void openMainPage();

	void onError();

	void writeToSharedPreferences(User user);

	void showAlertDialogBox(int mode, String title);

	void showProgressDialog(String message);

	void hideProgressDialog();

	void showToastMessage(String message);

}
