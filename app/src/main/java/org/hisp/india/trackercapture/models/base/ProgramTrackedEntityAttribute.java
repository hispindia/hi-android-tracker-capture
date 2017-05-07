package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.e_num.ValueType;

import java.io.Serializable;

/**
 * Created by nhancao on 5/7/17.
 */

public class ProgramTrackedEntityAttribute extends BaseModel implements Serializable {
    @SerializedName("mandatory")
    private boolean mandatory;
    @SerializedName("displayInList")
    private boolean displayInList;
    @SerializedName("allowFutureDate")
    private boolean allowFutureDate;
    @SerializedName("valueType")
    private String valueType;
    @SerializedName("trackedEntityAttribute")
    private TrackedEntityAttribute trackedEntityAttribute;

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

    public void setValueType(ValueType valueType) {
        this.valueType = valueType.name();
    }

    public boolean isAllowFutureDate() {
        return allowFutureDate;
    }

    public TrackedEntityAttribute getTrackedEntityAttribute() {
        return trackedEntityAttribute;
    }
}
