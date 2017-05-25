package org.hisp.india.trackercapture.models.base;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.models.storage.RProgramStageDataElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhancao on 5/21/17.
 */

public class Event extends BaseModel implements Serializable {
    @SerializedName("dataValues")
    private List<DataValue> dataValues;
    @SerializedName("dueDate")
    private String dueDate;
    @SerializedName("eventDate")
    private String eventDate;
    @SerializedName("enrollment")
    private String enrollmentId;
    @SerializedName("orgUnit")
    private String orgUnitId;
    @SerializedName("program")
    private String programId;
    @SerializedName("programStage")
    private String programStageId;
    @SerializedName("status")
    private String status = ProgramStatus.SCHEDULE.name();
    @SerializedName("trackedEntityInstance")
    private String trackedEntityInstanceId;

    public Event(RProgramStage programStage) {
        this.dueDate = programStage.getDueDate();
        this.eventDate = programStage.getEventDate();
        this.programStageId = programStage.getId();
        this.status = TextUtils.isEmpty(programStage.getStatus()) ? ProgramStatus.SCHEDULE.name() :
                      programStage.getStatus();
        this.setDataValues(getDataValueList(programStage.getProgramStageDataElements()));
    }

    public Event(String dueDate, String programStageId) {
        this.dueDate = dueDate;
        this.programStageId = programStageId;
    }

    public Event(String dueDate, String enrollmentId, String orgUnitId, String programId,
                 String programStageId, String trackedEntityInstanceId) {
        this.dueDate = dueDate;
        this.enrollmentId = enrollmentId;
        this.orgUnitId = orgUnitId;
        this.programId = programId;
        this.programStageId = programStageId;
        this.trackedEntityInstanceId = trackedEntityInstanceId;
    }

    public ArrayList<DataValue> getDataValueList(List<RProgramStageDataElement> programStageDataElementList) {
        ArrayList<DataValue> res = new ArrayList<>();
        for (RProgramStageDataElement programStageDataElement : programStageDataElementList) {
            DataValue dataValue = new DataValue(programStageDataElement.getValue(),
                                                programStageDataElement.getDataElement().getId(),
                                                programStageDataElement.isAllowProvidedElsewhere());
            res.add(dataValue);
        }
        return res;
    }

    public void setDataValues(List<DataValue> dataValues) {
        this.dataValues = dataValues;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public void setOrgUnitId(String orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public void setProgramStageId(String programStageId) {
        this.programStageId = programStageId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTrackedEntityInstanceId(String trackedEntityInstanceId) {
        this.trackedEntityInstanceId = trackedEntityInstanceId;
    }
}
