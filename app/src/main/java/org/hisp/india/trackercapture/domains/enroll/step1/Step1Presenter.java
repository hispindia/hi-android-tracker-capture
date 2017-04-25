package org.hisp.india.trackercapture.domains.enroll.step1;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.models.storage.TOrganizationUnit;
import org.hisp.india.trackercapture.models.storage.TProgram;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.services.programs.ProgramService;

import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

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


}
