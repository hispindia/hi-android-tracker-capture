package org.hisp.india.trackercapture.models.storage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskAttribute extends RealmObject implements Serializable {

    @SerializedName("attribute")
    private String attributeId;
    @SerializedName("value")
    private String value;

    @Expose(serialize = false, deserialize = false)
    private String displayName;

    @Expose(serialize = false, deserialize = false)
    private String valueType;

    public static RTaskAttribute create(String attributeId, String value, String displayName, String valueType) {
        RTaskAttribute attribute = new RTaskAttribute();
        attribute.setAttributeId(attributeId);
        attribute.setValue(value);
        attribute.setDisplayName(displayName);
        attribute.setValueType(valueType);
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
