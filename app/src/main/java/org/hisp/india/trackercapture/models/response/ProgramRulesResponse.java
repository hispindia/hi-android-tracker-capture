package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.ProgramRule;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class ProgramRulesResponse {

    @Expose
    @SerializedName("programRules")
    private List<ProgramRule> programRules;

    public List<ProgramRule> getProgramRules() {
        return programRules;
    }
}
