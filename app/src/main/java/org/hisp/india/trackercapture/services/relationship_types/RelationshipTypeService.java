package org.hisp.india.trackercapture.services.relationship_types;

import org.hisp.india.trackercapture.models.response.RelationshipTypesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface RelationshipTypeService {

    void setRestService(RelationshipTypeApi relationshipTypeApi);

    Observable<RelationshipTypesResponse> getRelationshipTypes();

}
