package org.hisp.india.trackercapture.models.storage;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskAttribute extends RealmObject {

    @SerializedName("attribute")
    private String attributeId;
    @SerializedName("value")
    private String value;

    public static RTaskAttribute create(String attributeId, String value) {
        RTaskAttribute attribute = new RTaskAttribute();
        attribute.setAttributeId(attributeId);
        attribute.setValue(value);
        return attribute;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
