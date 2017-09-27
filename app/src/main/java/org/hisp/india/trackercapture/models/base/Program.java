package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.response.ProgramStage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class Program extends BaseModel implements Serializable {

    @Expose
    @SerializedName("withoutRegistration")
    private boolean withoutRegistration;
    @Expose
    @SerializedName("enrollmentDateLabel")
    private String enrollmentDateLabel;
    @Expose
    @SerializedName("incidentDateLabel")
    private String incidentDateLabel;
    @Expose
    @SerializedName("selectEnrollmentDatesInFuture")
    private boolean selectEnrollmentDatesInFuture;
    @Expose
    @SerializedName("selectIncidentDatesInFuture")
    private boolean selectIncidentDatesInFuture;
    @Expose
    @SerializedName("displayIncidentDate")
    private boolean displayIncidentDate;
    @Expose
    @SerializedName("trackedEntity")
    private TrackedEntity trackedEntity;
    @Expose
    @SerializedName("programStages")
    private List<ProgramStage> programStages;
    @Expose
    @SerializedName("programRuleVariables")
    private List<ProgramRuleVariable> programRuleVariables;
    @Expose
    @SerializedName("programTrackedEntityAttributes")
    private List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes;
    @Expose
    @SerializedName("programRules")
    private List<ProgramRule> programRules;

    public String getEnrollmentDateLabel() {
        return enrollmentDateLabel;
    }

    public String getIncidentDateLabel() {
        return incidentDateLabel;
    }

    public boolean isSelectEnrollmentDatesInFuture() {
        return selectEnrollmentDatesInFuture;
    }

    public boolean isSelectIncidentDatesInFuture() {
        return selectIncidentDatesInFuture;
    }

    public List<ProgramStage> getProgramStages() {
        return programStages;
    }

    public List<ProgramRuleVariable> getProgramRuleVariables() {
        return programRuleVariables;
    }

    public List<ProgramTrackedEntityAttribute> getProgramTrackedEntityAttributes() {
        return programTrackedEntityAttributes;
    }

    public List<ProgramRule> getProgramRules() {
        return programRules;
    }

    public boolean isDisplayIncidentDate() {
        return displayIncidentDate;
    }

    public boolean isWithoutRegistration() {
        return withoutRegistration;
    }

    public TrackedEntity getTrackedEntity() {
        return trackedEntity;
    }
}
