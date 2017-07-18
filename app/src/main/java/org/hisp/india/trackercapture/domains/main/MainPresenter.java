package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.e_num.SyncKey;
import org.hisp.india.trackercapture.models.response.OrganizationUnitsResponse;
import org.hisp.india.trackercapture.models.storage.RMapping;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RSync;
import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountQuery;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.filter.AuthenticationSuccessFilter;
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
import rx.functions.Action1;
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
    private int orgUnitTotalPages;

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
            List<ROrganizationUnit> currentOrgForUser = OrganizationQuery
                    .getAllOrganization();
            HashMap<String, Boolean> userOrgKeyMap = new HashMap<>();
            if (currentOrgForUser.size() > 0) {
                for (ROrganizationUnit rOrganizationUnit : currentOrgForUser) {
                    userOrgKeyMap.put(rOrganizationUnit.getId(), true);
                }
            }

            getView().showLoading("Retrieve all organization...");

            fetchOrganizationUnitsRecursion(1, userOrgKeyMap, organizationUnitsResponse -> {
                getView().syncSuccessful();
            });

        } else {
            getUserOrganizations();
        }
    }


    /**
     * Sync data
     */

    public void sync() {
        syncUserData();
    }

    public void syncUserData() {
        RxScheduler.onStop(subscription);
        getView().showLoading("Sync user data ...");
        subscription = accountService.login()
                                     .map(user -> {
                                         getView().updateProgressStatus("Save user data ...");
                                         return user;
                                     })
                                     .compose(
                                             new AuthenticationSuccessFilter(accountService.getCredentials()).execute())
                                     .compose(RxScheduler.applyIoSchedulers())
                                     .doOnTerminate(() -> {
                                         if (isViewAttached()) getView().hideLoading();
                                     })
                                     .subscribe(user -> {
                                         System.gc();
                                         syncOrganizationData();
                                     });
    }

    public void syncOrganizationData() {
        RxScheduler.onStop(subscription);

        ////@nhancv TODO: create map for current org of user
        List<ROrganizationUnit> currentOrgForUser = OrganizationQuery
                .getAllOrganization();
        HashMap<String, Boolean> userOrgKeyMap = new HashMap<>();
        if (currentOrgForUser.size() > 0) {
            for (ROrganizationUnit rOrganizationUnit : currentOrgForUser) {
                userOrgKeyMap.put(rOrganizationUnit.getId(), true);
            }
        }
        getView().showLoading("Retrieve all organization...");

        fetchOrganizationUnitsRecursion(1, userOrgKeyMap, organizationUnitsResponse -> {
            getView().syncSuccessful();
        });

    }

    public void fetchOrganizationUnitsRecursion(int page,
                                                HashMap<String, Boolean> userOrgKeyMap,
                                                Action1<? super OrganizationUnitsResponse> onNext) {
        String m = (orgUnitTotalPages <= 0) ? String.format("%s/%s", page, "-") :
                   String.format("%s/%s", page, orgUnitTotalPages);
        getView().hideCircleProgressView();
        getView().updateProgressStatus("Retrieve organization (" + m + ")...");
        organizationService.getOrganizationUnits(page)
                           .observeOn(Schedulers.computation())
                           .map(organizationUnitsResponse -> {
                               orgUnitTotalPages = organizationUnitsResponse.getPageResponse().getPageCount();
                               if (getView() != null) {
                                   getView().hideCircleProgressView();
                                   getView().updateProgressStatus(
                                           String.format("Saving organizations (%s/%s)...", page, orgUnitTotalPages));
                               }

                               List<ROrganizationUnit> rOrganizationUnits = new ArrayList<>();

                               for (OrganizationUnit organizationUnit :
                                       organizationUnitsResponse.getOrganizationUnits()) {
                                   if (!userOrgKeyMap.containsKey(organizationUnit.getId())) {
                                       rOrganizationUnits.add(RMapping.from(organizationUnit));
                                   }
                               }

                               RealmHelper.transaction(realm -> {
                                   realm.copyToRealmOrUpdate(rOrganizationUnits);

                                   if (page >= orgUnitTotalPages) {
                                       //update sync flag
                                       realm.copyToRealmOrUpdate(
                                               RSync.create(SyncKey.ROrganizationUnit, true));
                                   }
                               });

                               return organizationUnitsResponse;
                           })
                           .compose(RxScheduler.applyIoSchedulers())
                           .subscribe(organizationUnitsResponse -> {
                               if (page < orgUnitTotalPages) {
                                   fetchOrganizationUnitsRecursion(page + 1, userOrgKeyMap, onNext);
                               } else if (isViewAttached()) {
                                   getView().hideLoading();
                                   onNext.call(organizationUnitsResponse);
                               }
                           }, throwable -> {
                               if (isViewAttached()) getView().showError(throwable.getMessage());
                           });
        ;
    }

}
