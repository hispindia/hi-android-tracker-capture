package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nhancao on 5/9/17.
 */

public class TrackedEntityInstanceRequest {

    @SerializedName("trackedEntity")
    private String trackedEntityId;
    @SerializedName("orgUnit")
    private String orgUnitId;
    @SerializedName("attributes")
    private List<AttributeRequest> attributeRequestList;


}
