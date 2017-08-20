package org.hisp.india.trackercapture.services.sync;

import android.util.Log;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.request.EventRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.events.EventService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by nhancao on 7/11/17.
 */

public class DefaultSyncService implements SyncService {
    private static final String TAG = DefaultSyncService.class.getSimpleName();

    private EventService eventService;
    private EnrollmentService enrollmentService;
    private TrackedEntityInstanceService trackedEntityInstanceService;

    private Subscription subscription;

    public DefaultSyncService(TrackedEntityInstanceService trackedEntityInstanceService,
                              EnrollmentService enrollmentService,
                              EventService eventService) {
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.enrollmentService = enrollmentService;
        this.eventService = eventService;
    }

    @Override
    public void syncEnrollProgram() {

    }

    @Override
    public void syncEnrollProgram(String taskId, SyncCallback<RTaskRequest> syncCallback) {
        RTaskRequest taskRequest = SyncQuery.getRTaskRequest(taskId);
        if (taskRequest == null) {
            Log.e(TAG, "syncEnrollProgram: task is null");
            return;
        }

        RxScheduler.onStop(subscription);
        subscription = trackedEntityInstanceService
                .postTrackedEntityInstances(taskRequest.getTrackedEntityInstance())
                .observeOn(Schedulers.computation())
                .flatMap(baseResponse -> {
                    String trackedEntityInstanceId = baseResponse.getResponse().getReference();
                    if (trackedEntityInstanceId != null) {
                        taskRequest.getEnrollment()
                                   .setTrackedEntityInstanceId(trackedEntityInstanceId);
                        return enrollmentService.postEnrollments(taskRequest.getEnrollment())
                                                .compose(RxScheduler.applyIoSchedulers());
                    } else {
                        return Observable.just(baseResponse);
                    }
                })
                .flatMap(enrollmentResponse -> {
                    List<BaseResponse.Response> importSummaries = enrollmentResponse
                            .getResponse().getImportSummaries();

                    if (importSummaries != null && importSummaries.size() > 0) {

                        for (RTaskEvent event : taskRequest.getEventList()) {
                            event.setEnrollmentId(
                                    importSummaries.get(0).getReference());
                            event.setOrgUnitId(taskRequest.getEnrollment().getOrgUnitId());
                            event.setProgramId(taskRequest.getEnrollment().getProgramId());
                            event.setTrackedEntityInstanceId(
                                    taskRequest.getEnrollment().getTrackedEntityInstanceId());
                        }

                        EventRequest eventRequest = new EventRequest(taskRequest.getEventList());
                        return eventService.postEvents(eventRequest)
                                           .compose(RxScheduler.applyIoSchedulers());

                    } else {
                        return Observable.just(enrollmentResponse);
                    }
                })
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(baseResponse -> {
                               syncCallback.succeed(taskRequest);
                           },
                           throwable -> {
                               syncCallback.error(taskRequest, throwable.toString());
                           });
    }

}
