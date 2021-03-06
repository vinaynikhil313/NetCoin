package com.netcoin.login.login.email.interactor;

import com.netcoin.login.login.email.presenter.OnEmailLoginFinishedListener;

/**
 * Created by Vinay Nikhil Pabba on 21-01-2016.
 */
public interface EmailLoginInteractor {

	void authenticateWithEmail(String email, String password, OnEmailLoginFinishedListener listener);

	void validatePhoneNo(String phoneNo);
}
