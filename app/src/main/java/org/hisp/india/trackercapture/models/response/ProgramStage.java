package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 5/6/17.
 */

public class ProgramStage extends BaseModel implements Serializable {
    @Expose
    @SerializedName("sortOrder")
    private int sortOrder;
    @Expose
    @SerializedName("minDaysFromStart")
    private int minDaysFromStart;
    @Expose
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
