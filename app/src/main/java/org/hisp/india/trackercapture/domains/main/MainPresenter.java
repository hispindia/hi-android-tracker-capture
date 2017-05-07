package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.models.query.UserQuery;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.organization.OrganizationModel;

import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 5/5/17.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private Router router;
    private NavigatorHolder navigatorHolder;

    private AccountService accountService;

    @Inject
    public MainPresenter(Router router, NavigatorHolder navigatorHolder, AccountService accountService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.accountService = accountService;
    }

    @Override
    public void attachView(MainView view) {
        super.attachView(view);
        navigatorHolder.setNavigator(view.getNavigator());
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        super.detachView(retainInstance);
    }

    public RUser getUserInfo() {
        return UserQuery.getUser();
    }

    public void logout() {
        accountService.logout();
        router.replaceScreen(Screens.LOGIN_SCREEN);
    }

    public void getOrganizations() {
        List<ROrganizationUnit> tOrganizationUnits;
        tOrganizationUnits = OrganizationModel.getAllOrganization();
        getView().showOrgList(tOrganizationUnits);
        if (tOrganizationUnits.size() > 0) {
            getView().showProgramList(tOrganizationUnits.get(0).getPrograms());
        }
    }

}
