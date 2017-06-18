package org.hisp.india.trackercapture.domains.login;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RMapping;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Subscription;
import rx.schedulers.Schedulers;

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
    protected OrganizationService organizationService;


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
                                     .compose(RxScheduler.applyIoSchedulers())
                                     .doOnTerminate(() -> getView().hideLoading())
                                     .subscribe(user -> {
                                         getView().loginSuccessful(user);
                                         getAllOrgs(user.getOrganizationUnits());
                                     }, this::exportError);
    }

    public void getAllOrgs(List<OrganizationUnit> currentOrgForUser) {
        RxScheduler.onStop(subscription);
        getView().showLoading("Retrieve all organization ...");
        subscription = organizationService.getOrganizationUnits()
                                          .observeOn(Schedulers.computation())
                                          .map(organizationUnitsResponse -> {
                                              List<ROrganizationUnit> rOrganizationUnits = new ArrayList<>();

                                              ////@nhancv TODO: create map for current org of user
                                              HashMap<String, Boolean> userOrgKeyMap = new HashMap<>();
                                              if (currentOrgForUser != null && currentOrgForUser.size() > 0) {
                                                  for (OrganizationUnit organizationUnit : currentOrgForUser) {
                                                      userOrgKeyMap.put(organizationUnit.getId(), true);
                                                  }
                                              }

                                              for (OrganizationUnit organizationUnit :
                                                      organizationUnitsResponse.getOrganizationUnits()) {
                                                  if (!userOrgKeyMap.containsKey(organizationUnit.getId())) {
                                                      rOrganizationUnits.add(RMapping.from(organizationUnit));
                                                  }
                                              }

                                              RealmHelper.transaction(realm -> {
                                                  realm.copyToRealmOrUpdate(rOrganizationUnits);
                                              });
                                              return organizationUnitsResponse;
                                          })
                                          .compose(RxScheduler.applyIoSchedulers())
                                          .doOnTerminate(() -> getView().hideLoading())
                                          .subscribe(organizationUnitsResponse -> {
                                              router.replaceScreen(Screens.MAIN_SCREEN);
                                          }, this::exportError);

    }

    private void exportError(Throwable throwable) {
        accountService.logout();
        getView().loginError(throwable);
    }

}
