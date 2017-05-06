package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.TrackedEntityInstancesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultTrackedEntityInstanceService implements TrackedEntityInstanceService {

    private NetworkProvider networkProvider;
    private TrackedEntityInstanceApi restService;

    public DefaultTrackedEntityInstanceService(NetworkProvider networkProvider,
                                               TrackedEntityInstanceApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public Observable<TrackedEntityInstancesResponse> getTrackedEntityInstances() {
        return networkProvider
                .transformResponse(restService.getTrackedEntityInstances());
    }
}
