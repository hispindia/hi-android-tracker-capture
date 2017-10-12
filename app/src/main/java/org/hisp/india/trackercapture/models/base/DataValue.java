package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 5/24/17.
 */

public class DataValue implements Serializable {
    @Expose
    @SerializedName("value")
    private String value;
    @Expose
    @SerializedName("dataElement")
    private String dataElementId;
    @Expose
    @SerializedName("providedElsewhere")
    private boolean providedElsewhere;
    @Expose
    @SerializedName("storedBy")
    private String storedBy;

    public DataValue(String value, String dataElementId, boolean providedElsewhere) {
        this.value = value;
        this.dataElementId = dataElementId;
        this.providedElsewhere = providedElsewhere;
        this.storedBy = "android";
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
}
