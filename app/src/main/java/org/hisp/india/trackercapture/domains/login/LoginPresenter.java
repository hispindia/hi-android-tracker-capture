package org.hisp.india.trackercapture.domains.login;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.filter.AuthenticationSuccessFilter;

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
            login();
        } catch (Exception e) {
            getView().showErrorMessage(e.getMessage());
        }
    }

    public void login() {
        RxScheduler.onStop(subscription);
        getView().showLoading("Authentication ...");
        subscription = accountService.login()
                                     .map(user -> {
                                         getView().updateProgressStatus("Save user data ...");
                                         return user;
                                     })
                                     .compose(
                                             new AuthenticationSuccessFilter(accountService.getCredentials()).execute())
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
