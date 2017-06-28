package org.hisp.india.trackercapture.services.organization;

import org.hisp.india.trackercapture.models.response.OrganizationUnitsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/16/17.
 */

public interface OrganizationService {

    void setRestService(OrganizationApi organizationApi);

    Observable<OrganizationUnitsResponse> getOrganizationUnits();

}
