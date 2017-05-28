package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 5/10/17.
 */

public class QueryResponse implements Serializable {
    @SerializedName("headers")
    private List<HeaderResponse> headers;
    @SerializedName("metaData")
    private MetaDataResponse metaData;
    @SerializedName("rows")
    private List<List<String>> rows;

    @Expose
    private int position;

    public List<HeaderResponse> getHeaders() {
        return headers;
    }

    public MetaDataResponse getMetaData() {
        return metaData;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
               "headers=" + headers +
               ", metaData=" + metaData +
               ", rows=" + rows +
               '}';
    }
}
