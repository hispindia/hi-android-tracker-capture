package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.User;
import org.hisp.india.trackercapture.models.storage.RMapping;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.organization.OrganizationModel;
import org.hisp.india.trackercapture.services.programs.ProgramModel;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.services.programs.ProgramService;

import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Observable;

/**
 * Created by nhancao on 5/5/17.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private Router router;
    private NavigatorHolder navigatorHolder;

    private AccountService accountService;
    private OrganizationService organizationService;
    private ProgramService programService;

    @Inject
    public MainPresenter(Router router, NavigatorHolder navigatorHolder, AccountService accountService,
                         OrganizationService organizationService, ProgramService programService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.accountService = accountService;
        this.organizationService = organizationService;
        this.programService = programService;
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

    public User getUserInfo() {
        return accountService.getCredentials().getUserInfo();
    }

    public void logout() {
        accountService.logout();
        router.replaceScreen(Screens.LOGIN_SCREEN);
    }

    public void getOrganizations() {
        List<ROrganizationUnit> tOrganizationUnits;
        tOrganizationUnits = OrganizationModel.getAllOrganization();
        getView().showOrgList(tOrganizationUnits);

        getView().showLoading();
        organizationService.getOrganizationUnits()
                           .compose(RxScheduler.applyIoSchedulers())
                           .doOnTerminate(() -> getView().hideLoading())
                           .subscribe(organizationUnitsResponse -> {
                               Observable.from(organizationUnitsResponse.getOrganizationUnits())
                                         .map(RMapping::from)
                                         .toList()
                                         .map(organizationUnitList -> {
                                             OrganizationModel.insertOrUpdate(organizationUnitList);
                                             return organizationUnitList;
                                         }).subscribe(
                                       organizationUnitList -> getView().showOrgList(organizationUnitList));
                           }, Throwable::printStackTrace);


    }

    public void getPrograms() {
        List<RProgram> tPrograms;
        tPrograms = ProgramModel.getAllPrograms();
        getView().showProgramList(tPrograms);

        getView().showLoading();
        programService.getPrograms()
                      .compose(RxScheduler.applyIoSchedulers())
                      .doOnTerminate(() -> getView().hideLoading())
                      .subscribe(programsResponse -> {
                          Observable.from(programsResponse.getPrograms())
                                    .map(RMapping::from)
                                    .toList()
                                    .map(programList -> {
                                        ProgramModel.insertOrUpdate(programList);
                                        return programList;
                                    }).subscribe(programList -> getView().showProgramList(programList));
                      }, Throwable::printStackTrace);


    }

}
