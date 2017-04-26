package org.hisp.india.trackercapture.services.tracked_entity_attribute_groups;

import org.hisp.india.trackercapture.models.response.TrackedEntityAttributeGroupsResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface TrackedEntityAttributeGroupApi {

    @GET("api/trackedEntityAttributeGroups?paging=false")
    Observable<TrackedEntityAttributeGroupsResponse> getTrackedEntityAttributeGroups();

}
