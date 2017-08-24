package org.hisp.india.trackercapture.models.storage;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/24/17.
 */

public class RTrackedEntityInstance extends RealmObject {

    @PrimaryKey
    private String trackedEntityInstanceId;
    private String trackedEntityId;
    private String orgUnitId;
    private String programId;
    private RealmList<RAttribute> attributeList;

    public String getTrackedEntityInstanceId() {
        return trackedEntityInstanceId;
    }

    public void setTrackedEntityInstanceId(String trackedEntityInstanceId) {
        this.trackedEntityInstanceId = trackedEntityInstanceId;
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

    public String getProgramId() {
        return programId;
    }

    public RTrackedEntityInstance setProgramId(String programId) {
        this.programId = programId;
        return this;
    }

    public RealmList<RAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(RealmList<RAttribute> attributeList) {
        this.attributeList = attributeList;
    }


}
