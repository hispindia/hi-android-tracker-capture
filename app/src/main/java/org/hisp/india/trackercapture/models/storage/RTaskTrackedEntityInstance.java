package org.hisp.india.trackercapture.models.storage;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskTrackedEntityInstance extends RealmObject {
    @SerializedName("trackedEntity")
    private String trackedEntityId;
    @SerializedName("orgUnit")
    private String orgUnitId;
    @SerializedName("attributes")
    private List<RTaskAttribute> attributeRequestList;

    public static RTaskTrackedEntityInstance create(String trackedEntityId, String orgUnitId,
                                                    List<RTaskAttribute> attributeRequestList) {
        RTaskTrackedEntityInstance trackedEntityInstance = new RTaskTrackedEntityInstance();
        trackedEntityInstance.setTrackedEntityId(trackedEntityId);
        trackedEntityInstance.setOrgUnitId(orgUnitId);
        trackedEntityInstance.setAttributeRequestList(attributeRequestList);
        return trackedEntityInstance;
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

    public List<RTaskAttribute> getAttributeRequestList() {
        return attributeRequestList;
    }

    public void setAttributeRequestList(
            List<RTaskAttribute> attributeRequestList) {
        this.attributeRequestList = attributeRequestList;
    }

}
