package org.hisp.india.trackercapture.models.storage;

import com.google.gson.Gson;

import org.hisp.india.trackercapture.models.base.Model;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/24/17.
 */

public class RProgramStage extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String displayName;
    private int sortOrder;
    private int minDaysFromStart;
    private RealmList<RProgramStageDataElement> programStageDataElements;
    private RealmList<RProgramStageSection> programStageSections;

    @Ignore
    private String dueDate;
    @Ignore
    private String eventDate;
    @Ignore
    private String status = ProgramStatus.SCHEDULE.name();

    public static RProgramStage fromJson(String programStage) {
        return new Gson().fromJson(programStage, RProgramStage.class);
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

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getMinDaysFromStart() {
        return minDaysFromStart;
    }

    public void setMinDaysFromStart(int minDaysFromStart) {
        this.minDaysFromStart = minDaysFromStart;
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

    public RealmList<RProgramStageDataElement> getProgramStageDataElements() {
        return programStageDataElements;
    }

    public void setProgramStageDataElements(
            RealmList<RProgramStageDataElement> programStageDataElements) {
        this.programStageDataElements = programStageDataElements;
    }

    public RealmList<RProgramStageSection> getProgramStageSections() {
        return programStageSections;
    }

    public void setProgramStageSections(RealmList<RProgramStageSection> programStageSections) {
        this.programStageSections = programStageSections;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
