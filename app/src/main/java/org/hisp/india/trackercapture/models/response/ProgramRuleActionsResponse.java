package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.ProgramRuleAction;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class ProgramRuleActionsResponse {

    @Expose
    @SerializedName("programRuleActions")
    private List<ProgramRuleAction> programRuleActions;

    public List<ProgramRuleAction> getProgramRuleActions() {
        return programRuleActions;
    }
}
