package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.SerializedName;

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
}
