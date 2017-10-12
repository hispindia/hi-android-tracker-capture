package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.response.ProgramStageDataElement;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ahmed on 9/27/2017.
 */

public class ProgramStageSection extends BaseModel implements Serializable {
    @Expose
    @SerializedName("externalAccess")
    private boolean externalAccess;
    @Expose
    @SerializedName("sortOrder")
    private int sortOrder;
    @Expose
    @SerializedName("programStageDataElements")
    private List<ProgramStageDataElement> programStageDataElements;

    public boolean isExternalAccess() {
        return externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<ProgramStageDataElement> getProgramStageDataElements() {
        return programStageDataElements;
    }

    public void setProgramStageDataElements(List<ProgramStageDataElement> programStageDataElements) {
        this.programStageDataElements = programStageDataElements;
    }


}
