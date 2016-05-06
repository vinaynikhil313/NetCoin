package com.netcoin.login.login.facebook.interactor;

import com.netcoin.login.login.facebook.presenter.OnFacebookLoginFinishedListener;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface FacebookLoginInteractor {

    void requestData(OnFacebookLoginFinishedListener listener);

}
