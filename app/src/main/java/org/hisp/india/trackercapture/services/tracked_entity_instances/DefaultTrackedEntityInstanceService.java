package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.response.EventsResponse;
import org.hisp.india.trackercapture.models.response.QueryResponse;
import org.hisp.india.trackercapture.models.response.TrackedEntityInstancesResponse;
import org.hisp.india.trackercapture.models.storage.RTaskTrackedEntityInstance;
import org.hisp.india.trackercapture.models.storage.RTrackedEntityInstance;

import java.util.List;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultTrackedEntityInstanceService implements TrackedEntityInstanceService {

    private NetworkProvider networkProvider;
    private TrackedEntityInstanceApi restService;

    public DefaultTrackedEntityInstanceService(NetworkProvider networkProvider,
                                               TrackedEntityInstanceApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(TrackedEntityInstanceApi trackedEntityInstanceApi) {
        restService = trackedEntityInstanceApi;
    }

    @Override
    public Observable<TrackedEntityInstancesResponse> getTrackedEntityInstances(String orgUnitId,
                                                                                String programId) {
        return networkProvider
                .transformResponse(restService.getTrackedEntityInstances(orgUnitId, programId));
    }


    @Override
    public Observable<List<RTrackedEntityInstance>> getTrackedEntityInstancesLocal(String orgUnitId, String programId) {
        return Observable.defer(() -> Observable
                .just(TrackedEntityInstanceQuery.getTrackedEntityInstances(orgUnitId, programId)));
    }


    @Override
    public Observable<BaseResponse> postTrackedEntityInstances(
            RTaskTrackedEntityInstance trackedEntityInstanceRequest) {
        return networkProvider
                .transformResponse(restService.postTrackedEntityInstances(trackedEntityInstanceRequest));
    }

    @Override
    public Observable<BaseResponse> putTrackedEntityInstances(
            RTaskTrackedEntityInstance trackedEntityInstanceRequest) {
        return networkProvider
                .transformResponse(restService.putTrackedEntityInstances(trackedEntityInstanceRequest,
                                                                         trackedEntityInstanceRequest
                                                                                 .getTrackedEntityInstanceId()));
    }

    @Override
    public Observable<QueryResponse> queryTrackedEntityInstances(String orgUnitId, String programId,
                                                                 ProgramStatus programStatus) {
        return networkProvider
                .transformResponse(restService.queryTrackedEntityInstances(orgUnitId, programId, programStatus.name()));
    }

    //added by ifhaam 28/09/2017
    @Override
    public Observable<EventsResponse> getEvents(String orgUnitId,String trackedInstanceId){
        return networkProvider.transformResponse(restService.getEvents(orgUnitId, trackedInstanceId));
    }
}
