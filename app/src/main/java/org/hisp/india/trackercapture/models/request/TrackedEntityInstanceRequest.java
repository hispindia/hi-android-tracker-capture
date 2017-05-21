package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 5/9/17.
 */

public class TrackedEntityInstanceRequest implements Serializable {
    @SerializedName("trackedEntity")
    private String trackedEntityId;
    @SerializedName("orgUnit")
    private String orgUnitId;
    @SerializedName("attributes")
    private List<AttributeRequest> attributeRequestList;

    public TrackedEntityInstanceRequest(String trackedEntityId, String orgUnitId,
                                        List<AttributeRequest> attributeRequestList) {
        this.trackedEntityId = trackedEntityId;
        this.orgUnitId = orgUnitId;
        this.attributeRequestList = attributeRequestList;
    }

    public String getTrackedEntityId() {
        return trackedEntityId;
    }

    public void setTrackedEntityId(String trackedEntityId) {
        this.trackedEntityId = trackedEntityId;
    }

    public String getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(String orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public List<AttributeRequest> getAttributeRequestList() {
        return attributeRequestList;
    }

    public void setAttributeRequestList(
            List<AttributeRequest> attributeRequestList) {
        this.attributeRequestList = attributeRequestList;
    }
}
