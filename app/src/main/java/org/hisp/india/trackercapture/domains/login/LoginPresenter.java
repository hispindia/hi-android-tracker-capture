package org.hisp.india.trackercapture.domains.login;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.utils.RxHelper;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by nhancao on 5/5/17.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {
    private static final String TAG = LoginPresenter.class.getSimpleName();

    @Inject
    AccountService accountService;

    private Subscription subscription;

    @Inject
    public LoginPresenter() {
    }

    public Credentials getCredential() {
        return accountService.getCredentials();
    }

    public void updateCredentialHost(String host) {
        accountService.updateCredentialHost(host);
    }

    public void updateCredentialTokenAndLogin(String username, String password) {
        accountService.updateCredentialToken(username, password);
        login();
    }

    private void login() {
        RxHelper.onStop(subscription);
        getView().showLoading();
        subscription = accountService.login()
                .compose(RxHelper.applyIOSchedulers())
                .doOnTerminate(() -> getView().hideLoading())
                .subscribe(user -> getView().loginSuccessful(user),
                        throwable -> getView().loginError(throwable)
                );
    }

}
