package org.hisp.india.trackercapture.domains.enroll_program_stage;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Observable;
import rx.Subscription;

/**
 * Created by nhancao on 5/5/17.
 */

public class EnrollProgramStagePresenter extends MvpBasePresenter<EnrollProgramStageView> {
    private static final String TAG = EnrollProgramStagePresenter.class.getSimpleName();

    private NavigatorHolder navigatorHolder;
    private Router router;
    private EnrollmentService enrollmentService;
    private TrackedEntityInstanceService trackedEntityInstanceService;
    private Subscription subscription;

    @Inject
    public EnrollProgramStagePresenter(Router router, NavigatorHolder navigatorHolder,
                                       TrackedEntityInstanceService trackedEntityInstanceService,
                                       EnrollmentService enrollmentService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.enrollmentService = enrollmentService;
    }

    @Override
    public void attachView(EnrollProgramStageView view) {
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

    public void getProgramDetail(String programId) {
        if (isViewAttached()) {
            Observable
                    .defer(() -> Observable.just(ProgramQuery.getProgram(programId))
                                           .delay(500, TimeUnit.MILLISECONDS)
                                           .compose(RxScheduler.applyLogicSchedulers())
                                           .doOnSubscribe(() -> getView().showLoading())
                                           .doOnTerminate(() -> getView().hideLoading()))
                    .subscribe(program -> getView().getProgramDetail(program));

        }
    }


}
