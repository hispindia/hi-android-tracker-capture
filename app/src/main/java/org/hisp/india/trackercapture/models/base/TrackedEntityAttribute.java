package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 4/9/17.
 */

public class TrackedEntityAttribute extends BaseModel implements Serializable {
    @SerializedName("mandatory")
    private boolean mandatory;
    @SerializedName("displayInList")
    private boolean displayInList;
    @SerializedName("valueType")
    private String valueType;
}
