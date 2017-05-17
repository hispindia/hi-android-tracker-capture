package org.hisp.india.trackercapture.domains.enroll_program;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Observable;
import rx.Subscription;

/**
 * Created by nhancao on 5/5/17.
 */

public class EnrollProgramPresenter extends MvpBasePresenter<EnrollProgramView> {
    private static final String TAG = EnrollProgramPresenter.class.getSimpleName();

    private NavigatorHolder navigatorHolder;
    private Router router;
    private EnrollmentService enrollmentService;
    private TrackedEntityInstanceService trackedEntityInstanceService;
    private Subscription subscription;

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

    public void getProgramDetail(String programId) {
        if (isViewAttached()) {
            getView().showLoading();
            getView().getProgramDetail(ProgramQuery.getProgram(programId));
            getView().hideLoading();
        }
    }

    public void registerProgram(TrackedEntityInstanceRequest trackedEntityInstanceRequest,
                                EnrollmentRequest enrollmentRequest) {
        RxScheduler.onStop(subscription);
        getView().showLoading();
        subscription = trackedEntityInstanceService
                .postTrackedEntityInstances(trackedEntityInstanceRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> getView().hideLoading())
                .flatMap(baseResponse -> {
                    if (baseResponse.getResponse().getReference() != null) {
                        enrollmentRequest.setTrackedEntityInstanceId(baseResponse.getResponse().getReference());
                        return enrollmentService.postEnrollments(enrollmentRequest)
                                                .compose(RxScheduler.applyIoSchedulers());
                    } else {
                        return Observable.just(baseResponse);
                    }
                })
                .subscribe(baseResponse -> {
                    getView().registerProgramSuccess(baseResponse);
                    onBackCommandClick();
                });
    }


}
