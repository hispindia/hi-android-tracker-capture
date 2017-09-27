package org.hisp.india.trackercapture.models.storage;

import com.google.gson.Gson;

import org.hisp.india.trackercapture.models.base.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ahmed on 9/27/2017.
 */

public class RProgramStageSection extends RealmObject implements Model {
    @PrimaryKey
    private String id;
    private String displayName;
    private int sortOrder;
    private boolean externalAccess;
    private RealmList<RProgramStageDataElement> programStageDataElements;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isExternalAccess() {
        return externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public RealmList<RProgramStageDataElement> getProgramStageDataElements() {
        return programStageDataElements;
    }

    public void setProgramStageDataElements(RealmList<RProgramStageDataElement> programStageDataElements) {
        this.programStageDataElements = programStageDataElements;
    }
}
