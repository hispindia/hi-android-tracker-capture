package org.hisp.india.trackercapture.domains.enroll_program_stage;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.Event;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.EventRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.events.EventService;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import java.util.List;
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
    private EventService eventService;
    private EnrollmentService enrollmentService;
    private TrackedEntityInstanceService trackedEntityInstanceService;
    private Subscription subscription;

    @Inject
    public EnrollProgramStagePresenter(Router router, NavigatorHolder navigatorHolder,
                                       TrackedEntityInstanceService trackedEntityInstanceService,
                                       EnrollmentService enrollmentService,
                                       EventService eventService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.enrollmentService = enrollmentService;
        this.eventService = eventService;
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

    public void registerProgram(TrackedEntityInstanceRequest trackedEntityInstanceRequest,
                                EnrollmentRequest enrollmentRequest, List<Event> eventList) {
        RxScheduler.onStop(subscription);
        getView().showLoading();
        subscription = trackedEntityInstanceService
                .postTrackedEntityInstances(trackedEntityInstanceRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> getView().hideLoading())
                .flatMap(baseResponse -> {
                    String trackedEntityInstanceId = baseResponse.getResponse().getReference();
                    if (trackedEntityInstanceId != null) {
                        enrollmentRequest.setTrackedEntityInstanceId(trackedEntityInstanceId);
                        return enrollmentService.postEnrollments(enrollmentRequest)
                                                .compose(RxScheduler.applyIoSchedulers());
                    } else {
                        return Observable.just(baseResponse);
                    }
                })
                .flatMap(enrollmentResponse -> {
                    List<BaseResponse.Response> importSummaries = enrollmentResponse
                            .getResponse().getImportSummaries();

                    if (importSummaries != null && importSummaries.size() > 0) {

                        for (Event event : eventList) {
                            event.setEnrollmentId(
                                    importSummaries.get(0).getReference());
                            event.setOrgUnitId(enrollmentRequest.getOrgUnitId());
                            event.setProgramId(enrollmentRequest.getProgramId());
                            event.setTrackedEntityInstanceId(
                                    enrollmentRequest.getTrackedEntityInstanceId());
                        }

                        EventRequest eventRequest = new EventRequest(eventList);
                        return eventService.postEvents(eventRequest)
                                           .compose(RxScheduler.applyIoSchedulers());

                    } else {
                        return Observable.just(enrollmentResponse);
                    }
                })
                .subscribe(baseResponse -> {
                    if (isViewAttached()) {
                        getView().registerProgramSuccess();
                    }
                    router.exit();
                });
    }

}
