package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 4/9/17.
 */

public class Option extends BaseModel implements Serializable {
    @Expose
    @SerializedName("code")
    private String code;

    public Option(String id, String displayName) {
        super(id, displayName);
    }

    public Option(String id, String displayName, String code) {
        super(id, displayName);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
