package org.hisp.india.trackercapture.domains.enroll.step1;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.storage.TMapping;
import org.hisp.india.trackercapture.models.storage.TOrganizationUnit;
import org.hisp.india.trackercapture.models.storage.TProgram;
import org.hisp.india.trackercapture.services.organization.MOrganization;
import org.hisp.india.trackercapture.services.organization.MProgram;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.services.programs.ProgramService;

import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;
import rx.Observable;

/**
 * Created by nhancao on 4/20/17.
 */

public class Step1Presenter extends MvpBasePresenter<Step1View> {

    private static final String TAG = Step1Presenter.class.getSimpleName();

    private Router router;
    private OrganizationService organizationService;
    private ProgramService programService;

    private List<TOrganizationUnit> tOrganizationUnits;
    private List<TProgram> tPrograms;

    @Inject
    public Step1Presenter(Router router, OrganizationService organizationService, ProgramService programService) {
        this.router = router;
        this.organizationService = organizationService;
        this.programService = programService;
    }

    public void getOrganizations() {
        if (tOrganizationUnits == null) {
            tOrganizationUnits = MOrganization.getAllOrganization();
            getView().showOrgList(tOrganizationUnits);
        }
        getView().showLoading();
        organizationService.getOrganizationUnits()
                           .compose(RxScheduler.applyIoSchedulers())
                           .doOnTerminate(() -> getView().hideLoading())
                           .subscribe(organizationUnitsResponse -> {
                               Observable.from(organizationUnitsResponse.getOrganizationUnits())
                                         .map(TMapping::from)
                                         .toList()
                                         .map(organizationUnitList -> {
                                             MOrganization.insertOrUpdate(organizationUnitList);
                                             return organizationUnitList;
                                         }).subscribe(
                                       organizationUnitList -> getView().showOrgList(organizationUnitList));
                           }, Throwable::printStackTrace);


    }

    public void getPrograms() {
        if (tPrograms == null) {
            tPrograms = MProgram.getAllPrograms();
            getView().showProgramList(tPrograms);
        }
        getView().showLoading();
        programService.getPrograms()
                      .compose(RxScheduler.applyIoSchedulers())
                      .doOnTerminate(() -> getView().hideLoading())
                      .subscribe(programsResponse -> {
                          Observable.from(programsResponse.getPrograms())
                                    .map(TMapping::from)
                                    .toList()
                                    .map(programList -> {
                                        MProgram.insertOrUpdate(programList);
                                        return programList;
                                    }).subscribe(programList -> getView().showProgramList(programList));
                      }, Throwable::printStackTrace);


    }

}
