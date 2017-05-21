package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 5/9/17.
 */

public class AttributeRequest implements Serializable {
    @SerializedName("attribute")
    private String attributeId;
    @SerializedName("value")
    private String value;

    public AttributeRequest(String attributeId, String value) {
        this.attributeId = attributeId;
        this.value = value;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public String getValue() {
        return value;
    }
}
