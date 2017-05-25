package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.e_num.ProgramStatus;

import java.io.Serializable;

/**
 * Created by nhancao on 5/9/17.
 */

public class EnrollmentRequest implements Serializable {
    @SerializedName("trackedEntityInstance")
    private String trackedEntityInstanceId;
    @SerializedName("program")
    private String programId;
    @SerializedName("status")
    private String status = ProgramStatus.ACTIVE.name();
    @SerializedName("orgUnit")
    private String orgUnitId;
    @SerializedName("enrollmentDate")
    private String enrollmentDate;
    @SerializedName("incidentDate")
    private String incidentDate;

    public EnrollmentRequest(String programId, String orgUnitId, String enrollmentDate,
                             String incidentDate) {
        this.programId = programId;
        this.orgUnitId = orgUnitId;
        this.enrollmentDate = enrollmentDate;
        this.incidentDate = incidentDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrgUnitId() {
        return orgUnitId;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public String getIncidentDate() {
        return incidentDate;
    }
}
