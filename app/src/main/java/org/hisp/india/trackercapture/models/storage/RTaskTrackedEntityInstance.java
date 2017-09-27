package org.hisp.india.trackercapture.models.storage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskTrackedEntityInstance extends RealmObject implements Serializable {

    @Expose
    @SerializedName("trackedEntityInstance")
    private String trackedEntityInstanceId;
    @Expose
    @SerializedName("trackedEntity")
    private String trackedEntityId;
    @Expose
    @SerializedName("orgUnit")
    private String orgUnitId;
    @Expose
    @SerializedName("attributes")
    private RealmList<RTaskAttribute> attributeRequestList;

    private String programId;

    public RTaskTrackedEntityInstance() {
        attributeRequestList = new RealmList<>();
    }

    public static RTaskTrackedEntityInstance create(String trackedEntityId, String orgUnitId,
                                                    List<RTaskAttribute> attributeRequestList, String programId) {
        RTaskTrackedEntityInstance trackedEntityInstance = new RTaskTrackedEntityInstance();
        trackedEntityInstance.setTrackedEntityId(trackedEntityId);
        trackedEntityInstance.setOrgUnitId(orgUnitId);
        trackedEntityInstance.setAttributeRequestList(attributeRequestList);
        trackedEntityInstance.setProgramId(programId);
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

    public void setAttributeRequestList(List<RTaskAttribute> attributeRequestList) {
        this.attributeRequestList.clear();
        this.attributeRequestList.addAll(attributeRequestList);
    }

    public String getTrackedEntityInstanceId() {
        return trackedEntityInstanceId;
    }

    public void setTrackedEntityInstanceId(String trackedEntityInstanceId) {
        this.trackedEntityInstanceId = trackedEntityInstanceId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }
}
