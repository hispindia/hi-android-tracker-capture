package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.TrackedEntityInstancesResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultTrackedEntityInstanceService implements TrackedEntityInstanceService {

    private NetworkProvider networkProvider;
    private TrackedEntityInstanceApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultTrackedEntityInstanceService(NetworkProvider networkProvider,
                                               TrackedEntityInstanceApi restService,
                                               ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<TrackedEntityInstancesResponse> getTrackedEntityInstances() {
        return networkProvider
                .transformResponse(restService.getTrackedEntityInstances())
                .compose(apiErrorFilter.execute());
    }
}
