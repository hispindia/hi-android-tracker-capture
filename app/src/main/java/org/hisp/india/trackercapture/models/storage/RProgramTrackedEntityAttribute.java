package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Model;
import org.hisp.india.trackercapture.models.e_num.ValueType;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/24/17.
 */

public class RProgramTrackedEntityAttribute extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String displayName;
    private boolean mandatory;
    private boolean displayInList;
    private boolean allowFutureDate;
    private String valueType;
    private RTrackedEntityAttribute trackedEntityAttribute;

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

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isDisplayInList() {
        return displayInList;
    }

    public void setDisplayInList(boolean displayInList) {
        this.displayInList = displayInList;
    }

    public ValueType getValueType() {
        return ValueType.getType(valueType);
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType.name();
    }

    public RTrackedEntityAttribute getTrackedEntityAttribute() {
        return trackedEntityAttribute;
    }

    public void setTrackedEntityAttribute(
            RTrackedEntityAttribute trackedEntityAttribute) {
        this.trackedEntityAttribute = trackedEntityAttribute;
    }

    public boolean isAllowFutureDate() {
        return allowFutureDate;
    }

    public void setAllowFutureDate(boolean allowFutureDate) {
        this.allowFutureDate = allowFutureDate;
    }
}
