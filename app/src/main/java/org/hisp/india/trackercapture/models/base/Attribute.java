package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 5/9/17.
 */

public class Attribute implements Serializable {
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("attribute")
    private String attributeId;
    @SerializedName("valueType")
    private String valueType;
    @SerializedName("value")
    private String value;

    public String getDisplayName() {
        return displayName;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public String getValueType() {
        return valueType;
    }

    public String getValue() {
        return value;
    }
}
