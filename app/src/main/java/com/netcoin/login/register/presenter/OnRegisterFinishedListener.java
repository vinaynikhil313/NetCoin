package com.netcoin.login.register.presenter;

import com.netcoin.user.User;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface OnRegisterFinishedListener {

	void onSuccess(User user);

	void onFailure(int errorCode);

	void onOTPReceived(String OTP);

}
