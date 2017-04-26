package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.TrackedEntityAttributeGroup;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class TrackedEntityAttributeGroupsResponse {

    @SerializedName("trackedEntityAttributeGroups")
    private List<TrackedEntityAttributeGroup> trackedEntityAttributeGroups;

    public List<TrackedEntityAttributeGroup> getTrackedEntityAttributeGroups() {
        return trackedEntityAttributeGroups;
    }
}
