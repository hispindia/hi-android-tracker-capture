package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.e_num.ValueType;

import java.io.Serializable;

/**
 * Created by nhancao on 5/10/17.
 */

public class DataElement extends BaseModel implements Serializable {
    @Expose
    @SerializedName("valueType")
    private String valueType;
    @Expose
    @SerializedName("optionSetValue")
    private boolean optionSetValue;
    @Expose
    @SerializedName("optionSet")
    private OptionSet optionSet;

    public ValueType getValueType() {
        return ValueType.getType(valueType);
    }

    public boolean isOptionSetValue() {
        return optionSetValue;
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }
}
