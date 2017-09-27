package org.hisp.india.trackercapture.models.storage;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.e_num.ProgramStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskEvent extends RealmObject implements Serializable {

    @Expose
    @SerializedName("dataValues")
    private RealmList<RTaskDataValue> dataValues;
    @Expose
    @SerializedName("dueDate")
    private String dueDate;
    @Expose
    @SerializedName("eventDate")
    private String eventDate;
    @Expose
    @SerializedName("enrollment")
    private String enrollmentId;
    @Expose
    @SerializedName("orgUnit")
    private String orgUnitId;
    @Expose
    @SerializedName("program")
    private String programId;
    @Expose
    @SerializedName("programStage")
    private String programStageId;
    @Expose
    @SerializedName("status")
    private String status = ProgramStatus.SCHEDULE.name();
    @Expose
    @SerializedName("trackedEntityInstance")
    private String trackedEntityInstanceId;

    public RTaskEvent() {
        dataValues = new RealmList<>();
    }

    public static RTaskEvent create(RProgramStage programStage) {
        RTaskEvent taskEvent = new RTaskEvent();
        taskEvent.setDueDate(programStage.getDueDate());
        taskEvent.setEventDate(programStage.getEventDate());
        taskEvent.setProgramStageId(programStage.getId());
        taskEvent.setStatus(TextUtils.isEmpty(programStage.getStatus()) ? ProgramStatus.SCHEDULE.name() :
                programStage.getStatus());
        taskEvent.setDataValues(taskEvent.getDataValueList(programStage.getProgramStageDataElements()));
        return taskEvent;
    }

    public ArrayList<RTaskDataValue> getDataValueList(List<RProgramStageDataElement> programStageDataElementList) {
        ArrayList<RTaskDataValue> res = new ArrayList<>();
        for (RProgramStageDataElement programStageDataElement : programStageDataElementList) {
            RTaskDataValue dataValue = RTaskDataValue.create(programStageDataElement.getValue(),
                    programStageDataElement.getDataElement().getId(),
                    programStageDataElement.isAllowProvidedElsewhere());
            res.add(dataValue);
        }
        return res;
    }

    public List<RTaskDataValue> getDataValues() {
        return dataValues;
    }

    public void setDataValues(RealmList<RTaskDataValue> dataValues) {
        this.dataValues = dataValues;
    }

    public void setDataValues(List<RTaskDataValue> dataValues) {
        this.dataValues.clear();
        this.dataValues.addAll(dataValues);
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
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

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramStageId() {
        return programStageId;
    }

    public void setProgramStageId(String programStageId) {
        this.programStageId = programStageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackedEntityInstanceId() {
        return trackedEntityInstanceId;
    }

    public void setTrackedEntityInstanceId(String trackedEntityInstanceId) {
        this.trackedEntityInstanceId = trackedEntityInstanceId;
    }
}
