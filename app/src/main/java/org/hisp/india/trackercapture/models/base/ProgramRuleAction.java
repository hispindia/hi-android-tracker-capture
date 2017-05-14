package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 4/9/17.
 */

public class ProgramRuleAction extends BaseModel implements Serializable {
    @SerializedName("programRuleActionType")
    private String programRuleActionType;

    public String getProgramRuleActionType() {
        return programRuleActionType;
    }
}
