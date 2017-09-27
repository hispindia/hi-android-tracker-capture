package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.TrackedEntityAttribute;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class TrackedEntityAttributesResponse {

    @Expose
    @SerializedName("trackedEntityAttributes")
    private List<TrackedEntityAttribute> trackedEntityAttributes;

    public List<TrackedEntityAttribute> getTrackedEntityAttributes() {
        return trackedEntityAttributes;
    }
}
