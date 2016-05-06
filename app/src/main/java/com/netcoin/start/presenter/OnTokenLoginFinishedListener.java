package com.netcoin.start.presenter;

import com.netcoin.user.User;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface OnTokenLoginFinishedListener {

	void onLoginSuccessful(User user);

	void onLoginUnsuccessful();

}
