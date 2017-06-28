package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.response.QueryResponse;
import org.hisp.india.trackercapture.models.response.TrackedEntityInstancesResponse;

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
    public Observable<TrackedEntityInstancesResponse> getTrackedEntityInstances() {
        return networkProvider
                .transformResponse(restService.getTrackedEntityInstances());
    }

    @Override
    public Observable<BaseResponse> postTrackedEntityInstances(
            TrackedEntityInstanceRequest trackedEntityInstanceRequest) {
        return networkProvider
                .transformResponse(restService.postTrackedEntityInstances(trackedEntityInstanceRequest));
    }

    @Override
    public Observable<QueryResponse> queryTrackedEntityInstances(String orgUnitId, String programId,
                                                                 ProgramStatus programStatus) {
        return networkProvider
                .transformResponse(restService.queryTrackedEntityInstances(orgUnitId, programId, programStatus.name()));
    }
}
