package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 4/9/17.
 */

public class TrackedEntityAttribute extends BaseModel implements Serializable {
    @SerializedName("optionSetValue")
    private boolean optionSetValue;
    @SerializedName("optionSet")
    private OptionSet optionSet;

    public boolean isOptionSetValue() {
        return optionSetValue;
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }
}
