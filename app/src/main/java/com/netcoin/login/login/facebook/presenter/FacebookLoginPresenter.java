package com.netcoin.login.login.facebook.presenter;

import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface FacebookLoginPresenter extends OnFacebookLoginFinishedListener, FacebookCallback<LoginResult> {

	void onAlertDialogTextEntered(int mode, String text);

}
