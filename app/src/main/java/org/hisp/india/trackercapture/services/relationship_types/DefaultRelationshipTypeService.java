package org.hisp.india.trackercapture.services.relationship_types;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.RelationshipTypesResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultRelationshipTypeService implements RelationshipTypeService {

    private NetworkProvider networkProvider;
    private RelationshipTypeApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultRelationshipTypeService(NetworkProvider networkProvider,
                                          RelationshipTypeApi restService,
                                          ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<RelationshipTypesResponse> getRelationshipTypes() {
        return networkProvider
                .transformResponse(restService.getRelationshipTypes())
                .compose(apiErrorFilter.execute());
    }
}
