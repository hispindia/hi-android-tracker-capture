package org.hisp.india.trackercapture.services.relationship_types;

import org.hisp.india.trackercapture.models.response.RelationshipTypesResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface RelationshipTypeApi {

    @GET("api/relationshipTypes?paging=false")
    Observable<RelationshipTypesResponse> getRelationshipTypes();

}
