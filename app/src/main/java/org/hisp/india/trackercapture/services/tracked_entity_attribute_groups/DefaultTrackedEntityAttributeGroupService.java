package org.hisp.india.trackercapture.services.tracked_entity_attribute_groups;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.TrackedEntityAttributeGroupsResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultTrackedEntityAttributeGroupService implements TrackedEntityAttributeGroupService {

    private NetworkProvider networkProvider;
    private TrackedEntityAttributeGroupApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultTrackedEntityAttributeGroupService(NetworkProvider networkProvider,
                                                     TrackedEntityAttributeGroupApi restService,
                                                     ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<TrackedEntityAttributeGroupsResponse> getTrackedEntityAttributeGroups() {
        return networkProvider
                .transformResponse(restService.getTrackedEntityAttributeGroups())
                .compose(apiErrorFilter.execute());
    }
}
