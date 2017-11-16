package org.hisp.india.trackercapture.services.sync;

import android.util.Log;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.Event;
import org.hisp.india.trackercapture.models.request.EventRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.events.EventService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by nhancao on 7/11/17.
 */

public class DefaultSyncService implements SyncService {
    private static final String TAG = DefaultSyncService.class.getSimpleName();

    private NetworkProvider networkProvider;
    private EventService eventService;
    private EnrollmentService enrollmentService;
    private TrackedEntityInstanceService trackedEntityInstanceService;

    private Subscription subscription;

    public DefaultSyncService(NetworkProvider networkProvider,
                              TrackedEntityInstanceService trackedEntityInstanceService,
                              EnrollmentService enrollmentService,
                              EventService eventService) {
        this.networkProvider = networkProvider;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.enrollmentService = enrollmentService;
        this.eventService = eventService;
    }

    @Override
    public void syncUnResolveEnrollProgram(SyncCallback<RTaskRequest> syncCallback) {
        if (networkProvider.isNetworkAvailable()) {
            RTaskRequest taskRequest = SyncQuery.getNextTaskRequest();
            if (taskRequest == null) {
                syncCallback.error("Done");
                return;
            }

            if (!taskRequest.isHadSynced() ){
                if(taskRequest.getTrackedEntityInstance().getTrackedEntityInstanceId()!=null){
                    registerToNewProgram(taskRequest,syncCallback);
                }else{
                    registerProgram(taskRequest, syncCallback);
                }
                // removed by ifhaam registerProgram(taskRequest, syncCallback);
            } else {
                updateProgram(taskRequest, syncCallback);
            }
        } else {
            syncCallback.error("No network");
        }
    }

    @Override
    public void syncEnrollProgram(String taskId, SyncCallback<RTaskRequest> syncCallback) {
        RTaskRequest taskRequest = SyncQuery.getRTaskRequest(taskId);
        if (taskRequest == null) {
            Log.e(TAG, "syncEnrollProgram: task is null");
            return;
        }
        if (!taskRequest.isHadSynced()){
            if(taskRequest.getTrackedEntityInstance().getTrackedEntityInstanceId()!=null){
                registerToNewProgram(taskRequest,syncCallback);
            }else{
                registerProgram(taskRequest, syncCallback);
            }
            // removed by ifhaam registerProgram(taskRequest, syncCallback);
        } else {
            updateProgram(taskRequest, syncCallback);
        }
    }

    public void registerProgram(RTaskRequest taskRequest, SyncCallback<RTaskRequest> syncCallback) {
        RxScheduler.onStop(subscription);
        subscription = trackedEntityInstanceService
                .postTrackedEntityInstances(taskRequest.getTrackedEntityInstance())
                .observeOn(Schedulers.computation())
                .flatMap(baseResponse -> {
                    List<BaseResponse.Response> importSummaries = baseResponse.getResponse().getImportSummaries();
                    if(importSummaries!=null && importSummaries.size()>0){
                        String trackedEntityInstanceId = importSummaries.get(0).getReference();
                        if (trackedEntityInstanceId != null) {
                            taskRequest.getTrackedEntityInstance()
                                    .setTrackedEntityInstanceId(trackedEntityInstanceId);
                            taskRequest.getEnrollment()
                                    .setTrackedEntityInstanceId(trackedEntityInstanceId);
                            return enrollmentService.postEnrollments(taskRequest.getEnrollment())
                                    .compose(RxScheduler.applyIoSchedulers());
                        }else{
                            return Observable.just(baseResponse);
                        }
                    }else {
                        return Observable.just(baseResponse);
                    }
                })
                .flatMap(enrollmentResponse -> {
                    List<BaseResponse.Response> importSummaries = enrollmentResponse
                            .getResponse().getImportSummaries();

                    if (importSummaries != null && importSummaries.size() > 0) {
                        String trackedEntityInstanceId = taskRequest.getEnrollment().getTrackedEntityInstanceId();
                        String enrollmentId = importSummaries.get(0).getReference();
                        taskRequest.getEnrollment().setEnrollmentId(enrollmentId);
                        for (RTaskEvent event : taskRequest.getEventList()) {

                            event.setEnrollmentId(enrollmentId);
                            event.setOrgUnitId(taskRequest.getEnrollment().getOrgUnitId());
                            event.setProgramId(taskRequest.getEnrollment().getProgramId());
                            event.setTrackedEntityInstanceId(trackedEntityInstanceId);
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
                            List<BaseResponse.Response> importSummaries = baseResponse.getResponse().getImportSummaries();
                            for(int i=0;i<taskRequest.getEventList().size();i++){
                                taskRequest.getEventList().get(i)
                                        .setEvent(importSummaries.get(i).getReference());
                            }
                            taskRequest.updateSyncStatus(true, null);
                            taskRequest.save();

                            syncCallback.succeed(taskRequest);
                        },
                        throwable -> {
                            taskRequest.updateSyncStatus(false, throwable.toString());
                            taskRequest.save();

                            syncCallback.error(throwable.toString());
                        });

    }

    public void registerToNewProgram(RTaskRequest taskRequest, SyncCallback<RTaskRequest> syncCallback){
        RxScheduler.onStop(subscription);
        subscription = trackedEntityInstanceService
                .putTrackedEntityInstances(taskRequest.getTrackedEntityInstance())
                .observeOn(Schedulers.computation())
                .flatMap(baseResponse -> {
                    String trackedEntityInstanceId = baseResponse.getResponse().getReference();
                    if (trackedEntityInstanceId != null) {
                        taskRequest.getTrackedEntityInstance()
                                .setTrackedEntityInstanceId(trackedEntityInstanceId);
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
                        String trackedEntityInstanceId = taskRequest.getEnrollment().getTrackedEntityInstanceId();
                        String enrollmentId = importSummaries.get(0).getReference();
                        taskRequest.getEnrollment().setEnrollmentId(enrollmentId);
                        for (RTaskEvent event : taskRequest.getEventList()) {

                            event.setEnrollmentId(enrollmentId);
                            event.setOrgUnitId(taskRequest.getEnrollment().getOrgUnitId());
                            event.setProgramId(taskRequest.getEnrollment().getProgramId());
                            event.setTrackedEntityInstanceId(trackedEntityInstanceId);
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
                            List<BaseResponse.Response> importSummaries = baseResponse.getResponse().getImportSummaries();
                            for(int i=0;i<taskRequest.getEventList().size();i++){
                                taskRequest.getEventList().get(i)
                                        .setEvent(importSummaries.get(i).getReference());
                            }
                            taskRequest.updateSyncStatus(true, null);
                            taskRequest.save();

                            syncCallback.succeed(taskRequest);
                        },
                        throwable -> {
                            taskRequest.updateSyncStatus(false, throwable.toString());
                            taskRequest.save();

                            syncCallback.error(throwable.toString());
                        });

    }

    public void updateProgram(RTaskRequest taskRequest, SyncCallback<RTaskRequest> syncCallback) {

        RxScheduler.onStop(subscription);
        subscription = trackedEntityInstanceService
                .putTrackedEntityInstances(taskRequest.getTrackedEntityInstance())
                .observeOn(Schedulers.computation())
                .compose(RxScheduler.applyIoSchedulers())
                .flatMap(baseResponse -> {
                    String trackedEntityInstanceId = baseResponse.getResponse().getReference();
                    if (trackedEntityInstanceId != null) {
                        taskRequest.getTrackedEntityInstance()
                                .setTrackedEntityInstanceId(trackedEntityInstanceId);
                        taskRequest.getEnrollment()
                                .setTrackedEntityInstanceId(trackedEntityInstanceId);
                        return enrollmentService.putEnrollments(taskRequest.getEnrollment())
                                .compose(RxScheduler.applyIoSchedulers());
                    } else {
                        return Observable.just(baseResponse);
                    }
                })
                .map(enrollmentResponse -> {
                    BaseResponse.Response importSummaries = enrollmentResponse
                            .getResponse();//.getImportSummaries();//removed by ifhaam

                    if (importSummaries != null ){//&& importSummaries.size() > 0) {
                        String trackedEntityInstanceId = taskRequest.getEnrollment().getTrackedEntityInstanceId();
                        String enrollmentId = importSummaries.getReference(); //.get(0).getReference();
                        taskRequest.getEnrollment().setEnrollmentId(enrollmentId);
                        for (RTaskEvent event : taskRequest.getEventList()) {
                            event.setEnrollmentId(enrollmentId);
                            event.setOrgUnitId(taskRequest.getEnrollment().getOrgUnitId());
                            event.setProgramId(taskRequest.getEnrollment().getProgramId());
                            event.setTrackedEntityInstanceId(trackedEntityInstanceId);
                        }

                       // EventRequest eventRequest = new EventRequest(taskRequest.getEventList());
                        //return eventService.putEvents(eventRequest, //trackedEntityInstanceId)
                          //      .compose(RxScheduler.applyIoSchedulers());

                        List<RTaskEvent> eventsToPost = new ArrayList<RTaskEvent>();
                        List<RTaskEvent> eventsToPut = new ArrayList<RTaskEvent>();
                        for(RTaskEvent rTaskEvent:taskRequest.getEventList()){
                            if(rTaskEvent.getEvent()==null){
                                eventsToPost.add(rTaskEvent);
                            }else{
                               eventsToPut.add(rTaskEvent);
                            }
                        }

                        Observable<BaseResponse> postSubscriptionsObservable = eventService.postEvents(new EventRequest(eventsToPost))
                                .compose(RxScheduler.applyIoSchedulers());

                        Observable<List<BaseResponse>> putSubscriptionsObservable = Observable.from(eventsToPut)
                                .flatMap(eventToPut-> eventService.putEvent(eventToPut,eventToPut.getEvent()))
                                .compose(RxScheduler.applyIoSchedulers())
                                .toList();


                        Observable.zip(postSubscriptionsObservable,putSubscriptionsObservable,
                                (postSubscriptions,putSubscriptions)->{
                                    List<BaseResponse.Response> responses = new ArrayList<BaseResponse.Response>();
                                    if(postSubscriptions.getResponse()!=null && postSubscriptions.getResponse().getImportSummaries()!=null){
                                        responses.addAll(((BaseResponse)postSubscriptions).getResponse().getImportSummaries());
                                    }
                                    for(BaseResponse baseResponse:(List<BaseResponse>)putSubscriptions){
                                        responses.add(baseResponse.getResponse());
                                    }

                                    return Observable.just(responses);
                                })
                                .compose(RxScheduler.applyIoSchedulers())
                                .subscribe(baseResponse -> {
                                            taskRequest.updateSyncStatus(true, null);
                                            taskRequest.save();

                                            syncCallback.succeed(taskRequest);
                                        },
                                        throwable -> {
                                            taskRequest.updateSyncStatus(false, throwable.toString());
                                            taskRequest.save();

                                            syncCallback.error(throwable.toString());
                                        });


                        /*return eventService.putEvent(event,event.getEvent())
                                .compose(RxScheduler.applyIoSchedulers());*/


                    } else {
                        Observable.just(enrollmentResponse)
                                .compose(RxScheduler.applyIoSchedulers())
                                .subscribe(baseResponse -> {
                                            taskRequest.updateSyncStatus(true, null);
                                            taskRequest.save();

                                            syncCallback.succeed(taskRequest);
                                        },
                                        throwable -> {
                                            taskRequest.updateSyncStatus(false, throwable.toString());
                                            taskRequest.save();

                                            syncCallback.error(throwable.toString());
                                        });
                    }
                    return enrollmentResponse;
                })
        .subscribe();



    }


}
