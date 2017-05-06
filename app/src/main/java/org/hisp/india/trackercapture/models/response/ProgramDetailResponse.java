package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.BaseModel;
import org.hisp.india.trackercapture.models.base.ProgramRule;
import org.hisp.india.trackercapture.models.base.ProgramRuleVariable;
import org.hisp.india.trackercapture.models.base.TrackedEntityAttribute;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 5/6/17.
 */

public class ProgramDetailResponse extends BaseModel implements Serializable {
    @SerializedName("enrollmentDateLabel")
    private String enrollmentDateLabel;
    @SerializedName("incidentDateLabel")
    private String incidentDateLabel;
    @SerializedName("selectEnrollmentDatesInFuture")
    private boolean selectEnrollmentDatesInFuture;
    @SerializedName("selectIncidentDatesInFuture")
    private boolean selectIncidentDatesInFuture;

    @SerializedName("programStages")
    private List<ProgramStage> programStages;

    @SerializedName("programRuleVariables")
    private List<ProgramRuleVariable> programRuleVariables;

    @SerializedName("programTrackedEntityAttributes")
    private List<TrackedEntityAttribute> programTrackedEntityAttributes;

    @SerializedName("programRules")
    private List<ProgramRule> programRules;

}
