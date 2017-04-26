package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.TrackedEntityInstance;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class TrackedEntityInstancesResponse {

    @SerializedName("trackedEntityInstances")
    private List<TrackedEntityInstance> trackedEntityInstances;

    public List<TrackedEntityInstance> getTrackedEntityInstances() {
        return trackedEntityInstances;
    }
}
