package org.hisp.india.trackercapture.services.organization;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.OrganizationUnitsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/16/17.
 */

public class DefaultOrganizationService implements OrganizationService {

    private NetworkProvider networkProvider;
    private OrganizationApi restService;

    public DefaultOrganizationService(NetworkProvider networkProvider,
                                      OrganizationApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public Observable<OrganizationUnitsResponse> getOrganizationUnits() {
        return networkProvider
                .transformResponse(restService.getOrganizationUnits());
    }

}
