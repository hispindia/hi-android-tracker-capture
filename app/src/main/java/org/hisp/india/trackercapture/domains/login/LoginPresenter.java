package org.hisp.india.trackercapture.domains.login;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.services.account.AccountService;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by nhancao on 5/5/17.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {
    private static final String TAG = LoginPresenter.class.getSimpleName();

    private AccountService accountService;

    private Subscription subscription;

    @Inject
    public LoginPresenter(AccountService accountService) {
        this.accountService = accountService;
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

    public void login() {
        RxScheduler.onStop(subscription);
        getView().showLoading();
        subscription = accountService.login()
                                     .compose(RxScheduler.applyIoSchedulers())
                                     .doOnTerminate(() -> getView().hideLoading())
                                     .subscribe(user -> getView().loginSuccessful(user),
                                                throwable -> getView().loginError(throwable)
                                               );
    }

}
