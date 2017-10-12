package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ahmed on 10/3/2017.
 */

public class Enrollment extends BaseModel implements Serializable{
    @Expose
    @SerializedName("orgUnit")
    String orgUnit;

    @Expose
    @SerializedName("enrollment")
    String enrollment;

    @Expose
    @SerializedName("enrollmentDate")
    String enrollmentDate;

    @Expose
    @SerializedName("incidentDate")
    String incidentDate;

    @Expose
    @SerializedName("trackedEntityInstance")
    String trackedEntityInstance;



    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
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

    public String getTrackedEntityInstance() {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance(String trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }
}
