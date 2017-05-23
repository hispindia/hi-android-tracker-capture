package org.hisp.india.trackercapture.services.task;

import android.content.Intent;

import com.google.gson.Gson;
import com.nhancv.ntask.AbstractTaskService;
import com.nhancv.ntask.NTaskManager;
import com.nhancv.ntask.RTask;

import org.greenrobot.eventbus.EventBus;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.di.ApplicationComponent;
import org.hisp.india.trackercapture.models.base.Event;
import org.hisp.india.trackercapture.models.request.EventRequest;
import org.hisp.india.trackercapture.models.request.TaskRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.events.EventService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by nhancao on 5/23/17.
 */

public class TaskService extends AbstractTaskService {

    public static EventBus bus = new EventBus();
    private Gson gson;
    private EventService eventService;
    private EnrollmentService enrollmentService;
    private TrackedEntityInstanceService trackedEntityInstanceService;
    private Subscription subscription;

    private ApplicationComponent applicationComponent;

    @Override
    protected void doing(Intent intent) {
        initComponent();

        while (NTaskManager.hasNext()) {
            RTask rTask = NTaskManager.next();
            if (rTask != null) {
                //Do something
                System.out.println("Process: " + rTask.getId());

                TaskRequest taskRequest = gson.fromJson(rTask.getItemContent(), TaskRequest.class);
                postBus(BusProgress.LOADING);
                RxScheduler.onStop(subscription);
                subscription = trackedEntityInstanceService
                        .postTrackedEntityInstances(taskRequest.getTrackedEntityInstanceRequest())
                        .compose(RxScheduler.applyIoSchedulers())
                        .doOnTerminate(() -> postBus(BusProgress.FINISH))
                        .flatMap(baseResponse -> {
                            String trackedEntityInstanceId = baseResponse.getResponse().getReference();
                            if (trackedEntityInstanceId != null) {
                                taskRequest.getEnrollmentRequest().setTrackedEntityInstanceId(trackedEntityInstanceId);
                                return enrollmentService.postEnrollments(taskRequest.getEnrollmentRequest())
                                                        .compose(RxScheduler.applyIoSchedulers());
                            } else {
                                return Observable.just(baseResponse);
                            }
                        })
                        .flatMap(enrollmentResponse -> {
                            List<BaseResponse.Response> importSummaries = enrollmentResponse
                                    .getResponse().getImportSummaries();

                            if (importSummaries != null && importSummaries.size() > 0) {

                                for (Event event : taskRequest.getEventList()) {
                                    event.setEnrollmentId(
                                            importSummaries.get(0).getReference());
                                    event.setOrgUnitId(taskRequest.getEnrollmentRequest().getOrgUnitId());
                                    event.setProgramId(taskRequest.getEnrollmentRequest().getProgramId());
                                    event.setTrackedEntityInstanceId(
                                            taskRequest.getEnrollmentRequest().getTrackedEntityInstanceId());
                                }

                                EventRequest eventRequest = new EventRequest(taskRequest.getEventList());
                                return eventService.postEvents(eventRequest)
                                                   .compose(RxScheduler.applyIoSchedulers());

                            } else {
                                return Observable.just(enrollmentResponse);
                            }
                        })
                        .subscribe(baseResponse -> postBus(BusProgress.SUCCESS),
                                   throwable -> postBus(BusProgress.ERROR));

                //Set complete if do succeed
                NTaskManager.completeTask(rTask);
            }
        }
    }

    private void initComponent() {
        if (applicationComponent == null) {
            applicationComponent = ((MainApplication) getApplication()).getApplicationComponent();
            gson = new Gson();
            eventService = applicationComponent.eventService();
            enrollmentService = applicationComponent.enrollmentService();
            trackedEntityInstanceService = applicationComponent.trackedEntityInstanceService();

        }
    }

    private void postBus(BusProgress busProgress) {
        RxScheduler.runOnUi(o -> bus.post(busProgress));
    }


}