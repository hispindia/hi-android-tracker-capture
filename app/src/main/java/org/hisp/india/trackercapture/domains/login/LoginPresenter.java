package org.hisp.india.trackercapture.domains.login;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.RefreshCredentialService;
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

    @Inject
    protected Router router;
    @Inject
    protected NavigatorHolder navigatorHolder;
    @Inject
    protected AccountService accountService;
    @Inject
    protected RefreshCredentialService refreshCredentialService;

    protected Subscription subscription;

    @Inject
    public LoginPresenter() {
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
        try {
            accountService.updateCredentialToken(username, password);
            refreshCredentialService.updateCredentialToken();
            login();
        } catch (Exception e) {
            getView().showErrorMessage(e.getMessage());
        }
    }

    public void login() {
        RxScheduler.onStop(subscription);
        getView().showLoading("Authentication ...");
        subscription = accountService.login()
                                     .compose(RxScheduler.applyIoSchedulers())
                                     .doOnTerminate(() -> getView().hideLoading())
                                     .subscribe(user -> {
                                         getView().loginSuccessful(user);
                                         router.replaceScreen(Screens.MAIN_SCREEN);
                                     }, this::exportError);
    }

    private void exportError(Throwable throwable) {
        accountService.logout();
    }

}
