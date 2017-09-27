package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.BaseModel;
import org.hisp.india.trackercapture.models.base.DataElement;

import java.io.Serializable;

/**
 * Created by nhancao on 5/6/17.
 */

public class ProgramStageDataElement extends BaseModel implements Serializable {
    @Expose
    @SerializedName("displayInReports")
    private boolean displayInReports;
    @Expose
    @SerializedName("compulsory")
    private boolean compulsory;
    @Expose
    @SerializedName("allowProvidedElsewhere")
    private boolean allowProvidedElsewhere;
    @Expose
    @SerializedName("sortOrder")
    private int sortOrder;
    @Expose
    @SerializedName("allowFutureDate")
    private boolean allowFutureDate;
    @Expose
    @SerializedName("dataElement")
    private DataElement dataElement;

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

    public DataElement getDataElement() {
        return dataElement;
    }
}
