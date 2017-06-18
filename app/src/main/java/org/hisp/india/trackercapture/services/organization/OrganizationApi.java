package org.hisp.india.trackercapture.services.organization;

import org.hisp.india.trackercapture.models.response.OrganizationUnitsResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/16/17.
 */

public interface OrganizationApi {

    @GET("api/organisationUnits?fields=id,displayName,code,level,parent&paging=false")
    Observable<OrganizationUnitsResponse> getOrganizationUnits();

}
