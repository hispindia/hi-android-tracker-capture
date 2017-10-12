package org.hisp.india.trackercapture.models.storage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskDataValue extends RealmObject implements Serializable {

    //remove @primary key by ifhaam on 12/10/2017
    @Expose
    @SerializedName("dataElement")
    private String dataElementId;
    @Expose
    @SerializedName("value")
    private String value;
    @Expose
    @SerializedName("providedElsewhere")
    private boolean providedElsewhere;
    @Expose
    @SerializedName("storedBy")
    private String storedBy;

    public static RTaskDataValue create(String value, String dataElementId, boolean providedElsewhere) {
        RTaskDataValue dataValue = new RTaskDataValue();
        dataValue.setValue(value);
        dataValue.setDataElementId(dataElementId);
        dataValue.setProvidedElsewhere(providedElsewhere);
        dataValue.setStoredBy("android");
        return dataValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDataElementId() {
        return dataElementId;
    }

    public void setDataElementId(String dataElementId) {
        this.dataElementId = dataElementId;
    }

    public boolean isProvidedElsewhere() {
        return providedElsewhere;
    }

    public void setProvidedElsewhere(boolean providedElsewhere) {
        this.providedElsewhere = providedElsewhere;
    }

    public String getStoredBy() {
        return storedBy;
    }

    public void setStoredBy(String storedBy) {
        this.storedBy = storedBy;
    }
}
