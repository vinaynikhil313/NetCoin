package com.netcoin.login.register.interactor;

import com.netcoin.login.register.presenter.OnRegisterFinishedListener;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface RegisterInteractor {

    void registerUser(String email, String password, String phoneNo, OnRegisterFinishedListener listener);

}
