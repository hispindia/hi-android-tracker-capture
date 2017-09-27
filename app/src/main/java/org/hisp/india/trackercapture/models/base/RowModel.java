package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.response.HeaderResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 5/29/17.
 */

public class RowModel implements Serializable {
    @Expose
    @SerializedName("headers")
    private List<HeaderResponse> headers;
    @Expose
    @SerializedName("rows")
    private List<String> rows;

    public RowModel(List<HeaderResponse> headers, List<String> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public List<HeaderResponse> getHeaders() {
        return headers;
    }

    public List<String> getRows() {
        return rows;
    }
}
