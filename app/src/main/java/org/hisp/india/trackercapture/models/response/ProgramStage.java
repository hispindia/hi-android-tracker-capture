package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 5/6/17.
 */

public class ProgramStage extends BaseModel implements Serializable {
    @SerializedName("sortOrder")
    private int sortOrder;
    @SerializedName("minDaysFromStart")
    private int minDaysFromStart;
    @SerializedName("programStageDataElements")
    private List<ProgramStageDataElement> programStageDataElements;

    public int getSortOrder() {
        return sortOrder;
    }

    public int getMinDaysFromStart() {
        return minDaysFromStart;
    }

    public List<ProgramStageDataElement> getProgramStageDataElements() {
        return programStageDataElements;
    }
}
