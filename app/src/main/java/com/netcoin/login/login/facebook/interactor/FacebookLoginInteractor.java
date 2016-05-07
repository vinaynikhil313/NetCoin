package com.netcoin.login.login.facebook.interactor;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface FacebookLoginInteractor {

	void generateOTP(String phoneNo);

	void requestData();

}
