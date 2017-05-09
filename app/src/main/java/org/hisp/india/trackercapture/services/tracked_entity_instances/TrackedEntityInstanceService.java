package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.response.TrackedEntityInstancesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface TrackedEntityInstanceService {

    Observable<TrackedEntityInstancesResponse> getTrackedEntityInstances();

    Observable<BaseResponse> postTrackedEntityInstances(TrackedEntityInstanceRequest trackedEntityInstanceRequest);
}
