package org.hisp.india.trackercapture.services.organization;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.OrganizationUnitsResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/16/17.
 */

public class DefaultOrganizationService implements OrganizationService {

    private NetworkProvider networkProvider;
    private OrganizationApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultOrganizationService(NetworkProvider networkProvider,
                                      OrganizationApi restService,
                                      ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<OrganizationUnitsResponse> getOrganizationUnits() {
        return networkProvider
                .transformResponse(restService.getOrganizationUnits())
                .compose(apiErrorFilter.execute());
    }

}
