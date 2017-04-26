package org.hisp.india.trackercapture.services.tracked_entity_attributes;

import org.hisp.india.trackercapture.models.response.TrackedEntityAttributesResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface TrackedEntityAttributeApi {

    @GET("api/trackedEntityAttributes?paging=false")
    Observable<TrackedEntityAttributesResponse> getTrackedEntityAttributes();

}
