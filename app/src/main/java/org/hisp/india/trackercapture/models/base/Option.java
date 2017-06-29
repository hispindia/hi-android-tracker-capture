package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 4/9/17.
 */

public class Option extends BaseModel implements Serializable {
    @SerializedName("code")
    private String code;

    public String getCode() {
        return code;
    }
}
