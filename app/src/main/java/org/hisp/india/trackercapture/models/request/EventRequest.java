package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.storage.RTaskEvent;

import java.util.List;

/**
 * Created by nhancao on 5/21/17.
 */

public class EventRequest {
    @Expose
    @SerializedName("events")
    private List<RTaskEvent> events;

    public EventRequest(List<RTaskEvent> events) {
        this.events = events;
    }

    public List<RTaskEvent> getEvents() {
        return events;
    }
}
