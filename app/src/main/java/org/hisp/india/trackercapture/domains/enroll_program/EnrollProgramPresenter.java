package org.hisp.india.trackercapture.domains.enroll_program;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 5/5/17.
 */

public class EnrollProgramPresenter extends MvpBasePresenter<EnrollProgramView> {
    private static final String TAG = EnrollProgramPresenter.class.getSimpleName();

    private NavigatorHolder navigatorHolder;
    private Router router;
    private EnrollmentService enrollmentService;
    private TrackedEntityInstanceService trackedEntityInstanceService;

    @Inject
    public EnrollProgramPresenter(Router router, NavigatorHolder navigatorHolder,
                                  TrackedEntityInstanceService trackedEntityInstanceService,
                                  EnrollmentService enrollmentService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.enrollmentService = enrollmentService;
    }

    @Override
    public void attachView(EnrollProgramView view) {
        super.attachView(view);
        navigatorHolder.setNavigator(view.getNavigator());
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        super.detachView(retainInstance);
    }

    public void onBackCommandClick() {
        router.exit();
    }

    public void saveProgram(RProgram program) {
        ProgramQuery.saveProgram(program);
    }

    public void gotoProgramStage() {
        router.navigateTo(Screens.ENROLL_PROGRAM_STAGE);
    }

    public TrackedEntityInstanceService getTrackedEntityInstanceService() {
        return trackedEntityInstanceService;
    }
}
