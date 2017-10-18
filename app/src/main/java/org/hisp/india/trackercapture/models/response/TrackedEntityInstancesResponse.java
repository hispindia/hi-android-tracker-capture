package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.TrackedEntityInstance;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class TrackedEntityInstancesResponse {

    @Expose
    @SerializedName("trackedEntityInstances")
    private List<TrackedEntityInstance> trackedEntityInstances;
    @Expose
    @SerializedName("pager")
    private PageResponse pager;

    public List<TrackedEntityInstance> getTrackedEntityInstances() {
        return trackedEntityInstances;
    }

    public PageResponse getPager() {
        return pager;
    }
}
