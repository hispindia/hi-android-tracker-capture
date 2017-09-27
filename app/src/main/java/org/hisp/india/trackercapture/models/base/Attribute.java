package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 5/9/17.
 */

public class Attribute implements Serializable {
    @Expose
    @SerializedName("displayName")
    private String displayName;
    @Expose
    @SerializedName("attribute")
    private String attributeId;
    @Expose
    @SerializedName("valueType")
    private String valueType;
    @Expose
    @SerializedName("value")
    private String value;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
