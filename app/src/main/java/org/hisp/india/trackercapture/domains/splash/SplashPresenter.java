package org.hisp.india.trackercapture.domains.splash;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.services.account.AccountService;

import javax.inject.Inject;

/**
 * Created by nhancao on 4/11/17.
 */

public class SplashPresenter extends MvpBasePresenter<SplashView> {

    @Inject
    protected AccountService accountService;

    @Inject
    public SplashPresenter() {
    }

    public void gotoNextScreen() {

        if (accountService.isLogin()) {
            getView().goToMain();
        } else {
            getView().goToLogin();
        }

    }


}
