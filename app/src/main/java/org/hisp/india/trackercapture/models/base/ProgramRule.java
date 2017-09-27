package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class ProgramRule extends BaseModel implements Serializable {
    @Expose
    @SerializedName("condition")
    private String condition;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("programRuleActions")
    private List<ProgramRuleAction> programRuleActions;

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public List<ProgramRuleAction> getProgramRuleActions() {
        return programRuleActions;
    }
}
