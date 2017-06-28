package org.hisp.india.trackercapture.services.tracked_entity_attribute_groups;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.TrackedEntityAttributeGroupsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultTrackedEntityAttributeGroupService implements TrackedEntityAttributeGroupService {

    private NetworkProvider networkProvider;
    private TrackedEntityAttributeGroupApi restService;

    public DefaultTrackedEntityAttributeGroupService(NetworkProvider networkProvider,
                                                     TrackedEntityAttributeGroupApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(TrackedEntityAttributeGroupApi trackedEntityAttributeGroupApi) {
        restService = trackedEntityAttributeGroupApi;
    }

    @Override
    public Observable<TrackedEntityAttributeGroupsResponse> getTrackedEntityAttributeGroups() {
        return networkProvider
                .transformResponse(restService.getTrackedEntityAttributeGroups());
    }
}
