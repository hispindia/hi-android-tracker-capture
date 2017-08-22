package org.hisp.india.trackercapture.models.storage;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.e_num.ProgramStatus;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskEnrollment extends RealmObject implements Serializable {

    @SerializedName("enrollment")
    private String enrollmentId;
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

    public static RTaskEnrollment create(String programId, String orgUnitId, String enrollmentDate,
                                         String incidentDate) {
        RTaskEnrollment enrollment = new RTaskEnrollment();
        enrollment.setProgramId(programId);
        enrollment.setOrgUnitId(orgUnitId);
        enrollment.setEnrollmentDate(enrollmentDate);
        enrollment.setIncidentDate(incidentDate);
        return enrollment;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(String orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
}
