package org.hisp.india.trackercapture.services.tracked_entity_attribute_groups;

import org.hisp.india.trackercapture.models.response.TrackedEntityAttributeGroupsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface TrackedEntityAttributeGroupService {

    void setRestService(TrackedEntityAttributeGroupApi trackedEntityAttributeGroupApi);

    Observable<TrackedEntityAttributeGroupsResponse> getTrackedEntityAttributeGroups();

}
