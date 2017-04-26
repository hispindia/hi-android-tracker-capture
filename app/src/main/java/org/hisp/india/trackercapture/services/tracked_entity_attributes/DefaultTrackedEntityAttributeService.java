package org.hisp.india.trackercapture.services.tracked_entity_attributes;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.TrackedEntityAttributesResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultTrackedEntityAttributeService implements TrackedEntityAttributeService {

    private NetworkProvider networkProvider;
    private TrackedEntityAttributeApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultTrackedEntityAttributeService(NetworkProvider networkProvider,
                                                TrackedEntityAttributeApi restService,
                                                ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<TrackedEntityAttributesResponse> getTrackedEntityAttributes() {
        return networkProvider
                .transformResponse(restService.getTrackedEntityAttributes())
                .compose(apiErrorFilter.execute());
    }
}
