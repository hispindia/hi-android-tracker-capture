package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.e_num.ProgramStatus;

/**
 * Created by nhancao on 5/9/17.
 */

public class EnrollmentRequest {
    @SerializedName("trackedEntityInstance")
    private String trackedEntityInstanceId;
    @SerializedName("program")
    private String programId;
    @SerializedName("status")
    private String status;
    @SerializedName("orgUnit")
    private String orgUnitId;
    @SerializedName("enrollmentDate")
    private String enrollmentDate;
    @SerializedName("incidentDate")
    private String incidentDate;

    public EnrollmentRequest(String programId, String orgUnitId, String enrollmentDate,
                             String incidentDate) {
        this.programId = programId;
        this.status = ProgramStatus.ACTIVE.name();
        this.orgUnitId = orgUnitId;
        this.enrollmentDate = enrollmentDate;
        this.incidentDate = incidentDate;
    }

    public void setTrackedEntityInstanceId(String trackedEntityInstanceId) {
        this.trackedEntityInstanceId = trackedEntityInstanceId;
    }
}
