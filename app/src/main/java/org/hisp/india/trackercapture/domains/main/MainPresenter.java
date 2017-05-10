package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountQuery;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.organization.OrganizationQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Subscription;

/**
 * Created by nhancao on 5/5/17.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private Router router;
    private NavigatorHolder navigatorHolder;
    private AccountService accountService;
    private TrackedEntityInstanceService trackedEntityInstanceService;
    private Subscription subscription;

    @Inject
    public MainPresenter(Router router, NavigatorHolder navigatorHolder, AccountService accountService,
                         TrackedEntityInstanceService trackedEntityInstanceService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.accountService = accountService;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
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
        return AccountQuery.getUser();
    }

    public void logout() {
        accountService.logout();
        router.replaceScreen(Screens.LOGIN_SCREEN);
    }

    public void getOrganizations() {
        List<ROrganizationUnit> tOrganizationUnits;
        tOrganizationUnits = OrganizationQuery.getAllOrganization();
        getView().showOrgList(tOrganizationUnits);
        if (tOrganizationUnits.size() > 0) {
            getView().showProgramList(tOrganizationUnits.get(0).getPrograms());
        }
    }

    public void queryPrograms(String orgUnitId, String programId, ProgramStatus programStatus) {
        RxScheduler.onStop(subscription);
        getView().showLoading();
        subscription = trackedEntityInstanceService
                .queryTrackedEntityInstances(orgUnitId, programId, programStatus)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> getView().hideLoading())
                .subscribe(queryResponse -> {
                    getView().queryProgramSuccess(queryResponse);
                });
    }


}
