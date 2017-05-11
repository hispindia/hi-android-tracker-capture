package org.hisp.india.trackercapture.domains.login;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountService;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Subscription;

/**
 * Created by nhancao on 5/5/17.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {
    private static final String TAG = LoginPresenter.class.getSimpleName();

    private Router router;
    private NavigatorHolder navigatorHolder;

    private AccountService accountService;
    private Subscription subscription;

    @Inject
    public LoginPresenter(Router router, NavigatorHolder navigatorHolder, AccountService accountService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.accountService = accountService;
    }

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        navigatorHolder.setNavigator(view.getNavigator());
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        super.detachView(retainInstance);
    }

    public Credentials getCredential() {
        return accountService.getCredentials();
    }

    public void updateCredentialHost(String host) {
        try {
            accountService.updateCredentialHost(host);
        } catch (Exception e) {
            getView().showErrorMessage(e.getMessage());
        }
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
                                     .subscribe(user -> {
                                                    getView().loginSuccessful(user);
                                                    router.replaceScreen(Screens.MAIN_SCREEN);
                                                },
                                                throwable -> getView().loginError(throwable)
                                               );
    }

}
