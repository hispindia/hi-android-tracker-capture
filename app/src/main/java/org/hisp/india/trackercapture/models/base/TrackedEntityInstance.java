package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.request.AttributeRequest;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class TrackedEntityInstance implements Serializable {
    @SerializedName("trackedEntity")
    private String trackedEntityId;
    @SerializedName("trackedEntityInstance")
    private String trackedEntityInstanceyId;
    @SerializedName("orgUnit")
    private String orgUnitId;
    @SerializedName("attributes")
    private List<Attribute> attributeList;

    @Expose
    private String displayName;

    public String getTrackedEntityId() {
        return trackedEntityId;
    }

    public String getTrackedEntityInstanceyId() {
        return trackedEntityInstanceyId;
    }

    public String getOrgUnitId() {
        return orgUnitId;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
