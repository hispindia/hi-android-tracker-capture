package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by nhancao on 5/10/17.
 */

public class MetaDataResponse {
    @SerializedName("names")
    private Map<String, String> names;
    @SerializedName("pager")
    private PageResponse pager;

    public Map<String, String> getNames() {
        return names;
    }

    public PageResponse getPager() {
        return pager;
    }

    @Override
    public String toString() {
        return "MetaDataResponse{" +
               "names=" + names +
               ", pager=" + pager +
               '}';
    }
}
