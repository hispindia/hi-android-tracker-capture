package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.trackercapture.models.response.TrackedEntityInstancesResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface TrackedEntityInstanceApi {

    @GET("api/trackedEntityInstances?paging=false")
    Observable<TrackedEntityInstancesResponse> getTrackedEntityInstances();


}
