package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.Event;

import java.util.List;

/**
 * Created by Ahmed on 9/28/2017.
 */

public class EventsResponse {
    @Expose
    @SerializedName("events")
    private List<Event> events;

    public List<Event> getEvents(){
        return events;
    }
}
