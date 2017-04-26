package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.ProgramRuleVariable;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class ProgramRuleVariablesResponse {

    @SerializedName("programRuleVariables")
    private List<ProgramRuleVariable> programRuleVariables;

    public List<ProgramRuleVariable> getProgramRuleVariables() {
        return programRuleVariables;
    }
}
