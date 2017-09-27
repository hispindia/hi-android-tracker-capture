package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.RelationshipType;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class RelationshipTypesResponse {

    @Expose
    @SerializedName("relationshipTypes")
    private List<RelationshipType> relationshipTypes;

    public List<RelationshipType> getRelationshipTypes() {
        return relationshipTypes;
    }
}
