package com.netcoin.login.forgot.interactor;

import com.netcoin.login.forgot.presenter.OnPasswordResetFinishedListener;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface ForgotPasswordInteractor {

    void sendResetEmail(String email, OnPasswordResetFinishedListener listener);

    void changePassword(String email, String oldPassword, String newPassword);

}
