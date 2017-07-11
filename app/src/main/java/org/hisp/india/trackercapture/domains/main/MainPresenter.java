package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.e_num.SyncKey;
import org.hisp.india.trackercapture.models.storage.RMapping;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RSync;
import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountQuery;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.organization.OrganizationQuery;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.services.sync.SyncService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;
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

public class MainPresenter extends MvpBasePresenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    protected Router router;
    @Inject
    protected NavigatorHolder navigatorHolder;
    @Inject
    protected OrganizationService organizationService;
    @Inject
    protected AccountService accountService;
    @Inject
    protected TrackedEntityInstanceService trackedEntityInstanceService;
    @Inject
    protected SyncService syncService;

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

    public void sync() {
        RxScheduler.onStop(subscription);
        getView().showLoading();
        subscription = accountService.login()
                                     .compose(RxScheduler.applyIoSchedulers())
                                     .doOnTerminate(() -> getView().hideLoading())
                                     .subscribe(user -> getView().syncSuccessful());
    }

    public void getUserOrganizations() {
        List<ROrganizationUnit> tOrganizationUnits = OrganizationQuery.getUserOrganizations();
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

    public void fetchingAllOrgs() {
        RSync orgSync = syncService.getSyncRowByKey(SyncKey.ROrganizationUnit);
        if (orgSync == null || !orgSync.isStatus()) {
            RxScheduler.onStop(subscription);
            getView().showLoading("Retrieve all organization ...");
            subscription = organizationService.getOrganizationUnits()
                                              .observeOn(Schedulers.computation())
                                              .map(organizationUnitsResponse -> {
                                                  if (getView() != null) {
                                                      getView().updateProgressStatus("Saving organizations ...");
                                                  }
                                                  List<ROrganizationUnit> currentOrgForUser = OrganizationQuery
                                                          .getAllOrganization();

                                                  List<ROrganizationUnit> rOrganizationUnits = new ArrayList<>();

                                                  ////@nhancv TODO: create map for current org of user
                                                  HashMap<String, Boolean> userOrgKeyMap = new HashMap<>();
                                                  if (currentOrgForUser.size() > 0) {
                                                      for (ROrganizationUnit rOrganizationUnit : currentOrgForUser) {
                                                          userOrgKeyMap.put(rOrganizationUnit.getId(), true);
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

                                                      //update sync flag
                                                      realm.copyToRealmOrUpdate(
                                                              RSync.create(SyncKey.ROrganizationUnit, true));
                                                  });

                                                  return organizationUnitsResponse;
                                              })
                                              .compose(RxScheduler.applyIoSchedulers())
                                              .doOnTerminate(() -> getView().hideLoading())
                                              .subscribe(organizationUnitsResponse -> getUserOrganizations(),
                                                         throwable -> getView().showError(throwable.getMessage()));

        }
    }

}
