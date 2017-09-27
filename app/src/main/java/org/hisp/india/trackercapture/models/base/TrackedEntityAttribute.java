package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 4/9/17.
 */

public class TrackedEntityAttribute extends BaseModel implements Serializable {
    @Expose
    @SerializedName("optionSetValue")
    private boolean optionSetValue;
    @Expose
    @SerializedName("optionSet")
    private OptionSet optionSet;

    public boolean isOptionSetValue() {
        return optionSetValue;
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }
}
