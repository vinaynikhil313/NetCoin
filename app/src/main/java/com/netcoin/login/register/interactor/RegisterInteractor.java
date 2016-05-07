package com.netcoin.login.register.interactor;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface RegisterInteractor {

	void generateOTP(String phoneNo);

	void registerUser(String email, String password, String phoneNo);

}
