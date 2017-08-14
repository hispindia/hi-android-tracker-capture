package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
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
    @Expose
    private String value;
    @Expose
    private List<Attribute> attributePreview;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void initAttributePreview() {
        attributePreview = new ArrayList<>();
        if (attributeList == null || attributeList.size() == 0 || displayName == null) return;
        for (Attribute attribute : attributeList) {
            if (attribute.getDisplayName().equals(displayName)) {
                attributePreview.add(attribute);
                break;
            }
        }
        for (Attribute attribute : attributeList) {
            if (attributePreview.size() > 0 &&
                attributePreview.size() < 4 &&
                !attribute.getDisplayName().equals(displayName)) {
                attributePreview.add(attribute);
            }
        }
    }

    public List<Attribute> getAttributePreview() {
        return attributePreview;
    }
}
