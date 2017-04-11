package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.models.User;
import org.hisp.india.trackercapture.services.account.AccountService;

import javax.inject.Inject;

/**
 * Created by nhancao on 5/5/17.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {

    @Inject
    AccountService accountService;

    @Inject
    public MainPresenter() {
    }

    public User getUserInfo() {
        return accountService.getCredentials().getUserInfo();
    }

    public void logout() {
        accountService.logout();
    }
}
