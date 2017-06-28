package org.hisp.india.trackercapture.services.relationship_types;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.RelationshipTypesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultRelationshipTypeService implements RelationshipTypeService {

    private NetworkProvider networkProvider;
    private RelationshipTypeApi restService;

    public DefaultRelationshipTypeService(NetworkProvider networkProvider,
                                          RelationshipTypeApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(RelationshipTypeApi relationshipTypeApi) {
        restService = relationshipTypeApi;
    }

    @Override
    public Observable<RelationshipTypesResponse> getRelationshipTypes() {
        return networkProvider
                .transformResponse(restService.getRelationshipTypes());
    }
}
