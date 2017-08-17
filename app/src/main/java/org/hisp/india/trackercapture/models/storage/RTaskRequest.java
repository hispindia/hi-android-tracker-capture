package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskRequest extends RealmObject {

    @PrimaryKey
    private String uuid;
    private List<RTaskEvent> eventList;
    private RTaskEnrollment enrollment;
    private RTaskTrackedEntityInstance trackedEntityInstance;

    public static RTaskRequest create(RTaskTrackedEntityInstance trackedEntityInstance,
                                      RTaskEnrollment enrollment,
                                      List<RTaskEvent> eventList) {
        RTaskRequest taskRequest = new RTaskRequest();
        taskRequest.setTrackedEntityInstance(trackedEntityInstance);
        taskRequest.setEnrollment(enrollment);
        taskRequest.setEventList(eventList);
        return taskRequest;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<RTaskEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<RTaskEvent> eventList) {
        this.eventList = eventList;
    }

    public RTaskEnrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(RTaskEnrollment enrollment) {
        this.enrollment = enrollment;
    }

    public RTaskTrackedEntityInstance getTrackedEntityInstance() {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance(
            RTaskTrackedEntityInstance trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public void save() {
        RealmHelper.transaction(realm -> {
            realm.insertOrUpdate(this);
        });
    }


}
