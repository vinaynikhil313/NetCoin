package com.netcoin.login.register.presenter;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface RegisterPresenter {

    void createUser(String email, String password, String phoneNo);

	void onOTPEntered(String OTP);

}
