package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.e_num.ValueType;

import java.io.Serializable;

/**
 * Created by nhancao on 5/10/17.
 */

public class DataElement extends BaseModel implements Serializable {
    @SerializedName("valueType")
    private String valueType;
    @SerializedName("optionSetValue")
    private boolean optionSetValue;
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
