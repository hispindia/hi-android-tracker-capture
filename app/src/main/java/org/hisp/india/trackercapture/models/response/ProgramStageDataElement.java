package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.BaseModel;

import java.io.Serializable;

/**
 * Created by nhancao on 5/6/17.
 */

public class ProgramStageDataElement extends BaseModel implements Serializable {
    @SerializedName("displayInReports")
    private boolean displayInReports;
    @SerializedName("compulsory")
    private boolean compulsory;
    @SerializedName("allowProvidedElsewhere")
    private boolean allowProvidedElsewhere;
    @SerializedName("sortOrder")
    private int sortOrder;
    @SerializedName("allowFutureDate")
    private boolean allowFutureDate;

    public boolean isDisplayInReports() {
        return displayInReports;
    }

    public boolean isCompulsory() {
        return compulsory;
    }

    public boolean isAllowProvidedElsewhere() {
        return allowProvidedElsewhere;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isAllowFutureDate() {
        return allowFutureDate;
    }
}
