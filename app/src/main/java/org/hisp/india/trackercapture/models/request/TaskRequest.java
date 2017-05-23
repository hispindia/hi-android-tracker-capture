package org.hisp.india.trackercapture.models.request;

import com.google.gson.Gson;

import org.hisp.india.trackercapture.models.base.Event;

import java.util.List;

/**
 * Created by nhancao on 5/23/17.
 */

public class TaskRequest {

    private List<Event> eventList;
    private EnrollmentRequest enrollmentRequest;
    private TrackedEntityInstanceRequest trackedEntityInstanceRequest;

    public TaskRequest(
            TrackedEntityInstanceRequest trackedEntityInstanceRequest,
            EnrollmentRequest enrollmentRequest, List<Event> eventList) {
        this.trackedEntityInstanceRequest = trackedEntityInstanceRequest;
        this.enrollmentRequest = enrollmentRequest;
        this.eventList = eventList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public EnrollmentRequest getEnrollmentRequest() {
        return enrollmentRequest;
    }

    public TrackedEntityInstanceRequest getTrackedEntityInstanceRequest() {
        return trackedEntityInstanceRequest;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
