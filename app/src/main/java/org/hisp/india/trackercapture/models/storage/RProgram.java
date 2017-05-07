package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/24/17.
 */

public class RProgram extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String displayName;
    private boolean withoutRegistration;
    private String enrollmentDateLabel;
    private String incidentDateLabel;
    private boolean selectEnrollmentDatesInFuture;
    private boolean selectIncidentDatesInFuture;
    private boolean displayIncidentDate;
    private RealmList<RProgramStage> programStages;
    private RealmList<RProgramRuleVariable> programRuleVariables;
    private RealmList<RProgramTrackedEntityAttribute> programTrackedEntityAttributes;
    private RealmList<RProgramRule> programRules;

    public String getEnrollmentDateLabel() {
        return enrollmentDateLabel;
    }

    public void setEnrollmentDateLabel(String enrollmentDateLabel) {
        this.enrollmentDateLabel = enrollmentDateLabel;
    }

    public String getIncidentDateLabel() {
        return incidentDateLabel;
    }

    public void setIncidentDateLabel(String incidentDateLabel) {
        this.incidentDateLabel = incidentDateLabel;
    }

    public boolean isSelectEnrollmentDatesInFuture() {
        return selectEnrollmentDatesInFuture;
    }

    public void setSelectEnrollmentDatesInFuture(boolean selectEnrollmentDatesInFuture) {
        this.selectEnrollmentDatesInFuture = selectEnrollmentDatesInFuture;
    }

    public boolean isSelectIncidentDatesInFuture() {
        return selectIncidentDatesInFuture;
    }

    public void setSelectIncidentDatesInFuture(boolean selectIncidentDatesInFuture) {
        this.selectIncidentDatesInFuture = selectIncidentDatesInFuture;
    }

    public boolean isDisplayIncidentDate() {
        return displayIncidentDate;
    }

    public void setDisplayIncidentDate(boolean displayIncidentDate) {
        this.displayIncidentDate = displayIncidentDate;
    }

    public RealmList<RProgramStage> getProgramStages() {
        return programStages;
    }

    public void setProgramStages(RealmList<RProgramStage> programStages) {
        this.programStages = programStages;
    }

    public RealmList<RProgramRuleVariable> getProgramRuleVariables() {
        return programRuleVariables;
    }

    public void setProgramRuleVariables(
            RealmList<RProgramRuleVariable> programRuleVariables) {
        this.programRuleVariables = programRuleVariables;
    }

    public RealmList<RProgramTrackedEntityAttribute> getProgramTrackedEntityAttributes() {
        return programTrackedEntityAttributes;
    }

    public void setProgramTrackedEntityAttributes(
            RealmList<RProgramTrackedEntityAttribute> programTrackedEntityAttributes) {
        this.programTrackedEntityAttributes = programTrackedEntityAttributes;
    }

    public RealmList<RProgramRule> getProgramRules() {
        return programRules;
    }

    public void setProgramRules(RealmList<RProgramRule> programRules) {
        this.programRules = programRules;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isWithoutRegistration() {
        return withoutRegistration;
    }

    public void setWithoutRegistration(boolean withoutRegistration) {
        this.withoutRegistration = withoutRegistration;
    }
}
