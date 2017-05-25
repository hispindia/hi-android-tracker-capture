package org.hisp.india.trackercapture.models.base;

import com.google.gson.Gson;

import org.hisp.india.trackercapture.models.storage.RProgramStageDataElement;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 5/25/17.
 */

public class StageDetail implements Serializable {
    private String programStageId;
    private String dueDateValue;
    private String reportDateValue;
    private List<RProgramStageDataElement> programStageDataElement;

    public StageDetail(String programStageId, String dueDateValue, String reportDateValue,
                       List<RProgramStageDataElement> programStageDataElement) {
        this.programStageId = programStageId;
        this.dueDateValue = dueDateValue;
        this.reportDateValue = reportDateValue;
        this.programStageDataElement = programStageDataElement;
    }

    public static StageDetail fromJson(String stageDetail) {
        return new Gson().fromJson(stageDetail, StageDetail.class);
    }

    public String getProgramStageId() {
        return programStageId;
    }

    public String getDueDateValue() {
        return dueDateValue;
    }

    public String getReportDateValue() {
        return reportDateValue;
    }

    public List<RProgramStageDataElement> getProgramStageDataElement() {
        return programStageDataElement;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
