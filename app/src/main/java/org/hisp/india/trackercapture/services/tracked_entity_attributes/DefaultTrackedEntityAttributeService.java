package org.hisp.india.trackercapture.services.tracked_entity_attributes;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.TrackedEntityAttributesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultTrackedEntityAttributeService implements TrackedEntityAttributeService {

    private NetworkProvider networkProvider;
    private TrackedEntityAttributeApi restService;

    public DefaultTrackedEntityAttributeService(NetworkProvider networkProvider,
                                                TrackedEntityAttributeApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(TrackedEntityAttributeApi trackedEntityAttributeApi) {
        restService = trackedEntityAttributeApi;
    }

    @Override
    public Observable<TrackedEntityAttributesResponse> getTrackedEntityAttributes() {
        return networkProvider
                .transformResponse(restService.getTrackedEntityAttributes());
    }
}
