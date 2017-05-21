package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.Event;

import java.util.List;

/**
 * Created by nhancao on 5/21/17.
 */

public class EventRequest {
    @SerializedName("events")
    private List<Event> events;

    public EventRequest(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}
