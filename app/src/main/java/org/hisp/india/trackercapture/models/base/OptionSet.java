package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class OptionSet extends BaseModel implements Serializable {
    @Expose
    @SerializedName("valueType")
    private String valueType;
    @Expose
    @SerializedName("options")
    private List<Option> options;

    public String getValueType() {
        return valueType;
    }

    public List<Option> getOptions() {
        return options;
    }
}
