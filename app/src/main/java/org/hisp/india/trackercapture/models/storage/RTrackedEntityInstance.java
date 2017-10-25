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

    public static RTrackedEntityInstance create(RTaskTrackedEntityInstance taskTrackedEntityInstance){
        RTrackedEntityInstance trackedEntityInstance = new RTrackedEntityInstance();
        trackedEntityInstance.setTrackedEntityInstanceId(taskTrackedEntityInstance.getTrackedEntityInstanceId());
        trackedEntityInstance.setTrackedEntityId(taskTrackedEntityInstance.getTrackedEntityId());
        trackedEntityInstance.setOrgUnitId(taskTrackedEntityInstance.getOrgUnitId());
        trackedEntityInstance.setProgramId(taskTrackedEntityInstance.getProgramId());
        trackedEntityInstance.setAttributeList(new RealmList<>());
        for(RTaskAttribute taskAttribute : taskTrackedEntityInstance.getAttributeRequestList()){
            RAttribute attribute = new RAttribute();
            attribute.setAttributeId(taskAttribute.getAttributeId());
            attribute.setValue(taskAttribute.getValue());
            attribute.setDisplayName(taskAttribute.getDisplayName());
            attribute.setValueType(taskAttribute.getValueType());
            trackedEntityInstance.getAttributeList().add(attribute);
        }

        return trackedEntityInstance;

    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RTrackedEntityInstance that = (RTrackedEntityInstance) o;

        return trackedEntityInstanceId.equals(that.trackedEntityInstanceId);

    }

    @Override
    public int hashCode() {
        return trackedEntityInstanceId.hashCode();
    }
}
