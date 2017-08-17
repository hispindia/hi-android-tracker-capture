package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;
import java.util.UUID;

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
    private String createTime;
    private String updateTime;
    private String lastSyncTime;
    private boolean lastSyncStatus;
    private boolean hasSynced;
    private String lastError;


    public static RTaskRequest create(RTaskTrackedEntityInstance trackedEntityInstance,
                                      RTaskEnrollment enrollment,
                                      List<RTaskEvent> eventList) {
        RTaskRequest taskRequest = new RTaskRequest();
        taskRequest.setUuid(UUID.randomUUID().toString());
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public boolean isLastSyncStatus() {
        return lastSyncStatus;
    }

    public void setLastSyncStatus(boolean lastSyncStatus) {
        this.lastSyncStatus = lastSyncStatus;
    }

    public boolean isHasSynced() {
        return hasSynced;
    }

    public void setHasSynced(boolean hasSynced) {
        this.hasSynced = hasSynced;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public void save() {
        RealmHelper.transaction(realm -> {
            realm.insertOrUpdate(this);
        });
    }


}
