package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class ProgramRule extends BaseModel implements Serializable {
    @SerializedName("condition")
    private String condition;
    @SerializedName("description")
    private String description;
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
