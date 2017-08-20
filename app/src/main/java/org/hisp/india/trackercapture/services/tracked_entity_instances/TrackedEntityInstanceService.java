package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.response.QueryResponse;
import org.hisp.india.trackercapture.models.response.TrackedEntityInstancesResponse;
import org.hisp.india.trackercapture.models.storage.RTaskTrackedEntityInstance;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface TrackedEntityInstanceService {

    void setRestService(TrackedEntityInstanceApi trackedEntityInstanceApi);

    Observable<TrackedEntityInstancesResponse> getTrackedEntityInstances(String orgUnitId, String programId);

    Observable<BaseResponse> postTrackedEntityInstances(RTaskTrackedEntityInstance trackedEntityInstanceRequest);

    Observable<QueryResponse> queryTrackedEntityInstances(String orgUnitId, String programId,
                                                          ProgramStatus programStatus);
}
