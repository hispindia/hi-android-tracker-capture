package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.OptionSet;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class OptionSetsResponse {

    @SerializedName("optionSets")
    private List<OptionSet> optionSets;

    public List<OptionSet> getPrograms() {
        return optionSets;
    }
}
