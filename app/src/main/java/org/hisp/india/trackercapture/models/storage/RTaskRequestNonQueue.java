package org.hisp.india.trackercapture.models.storage;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ahmed on 10/16/2017.
 */

public class RTaskRequestNonQueue extends RealmObject {

    @PrimaryKey
    private String uuid;
    private RealmList<RTaskEvent> eventList;
    private RTaskEnrollment enrollment;
    private RTaskTrackedEntityInstance trackedEntityInstance;
    private String createTime;
    private String updateTime;
    private String lastSyncTime;
    private boolean lastSyncStatus;
    private boolean needSync;
    private boolean hadSynced;
    private String lastError;

    public RTaskRequestNonQueue(RTaskRequest taskRequest){
        this.uuid = taskRequest.getUuid();
        this.eventList = (RealmList<RTaskEvent>) taskRequest.getEventList();
        this.enrollment = taskRequest.getEnrollment();
        this.trackedEntityInstance =taskRequest.getTrackedEntityInstance();
        this.createTime = taskRequest.getCreateTime();
        this.updateTime = taskRequest.getUpdateTime();
        this.lastSyncTime = taskRequest.getLastSyncTime();
        this.lastSyncStatus =taskRequest.isLastSyncStatus();
        this.needSync = taskRequest.isNeedSync();
        this.hadSynced = taskRequest.isHadSynced();
        this.lastError = taskRequest.getLastError();
    }

    public static RTaskRequest getRTaskRequest(RTaskRequestNonQueue rTaskRequestNonQueue){
        RTaskRequest taskRequest = new RTaskRequest();
        taskRequest.setUuid(rTaskRequestNonQueue.getUuid());
        taskRequest.setEventList(rTaskRequestNonQueue.getEventList());
        taskRequest.setEnrollment(rTaskRequestNonQueue.getEnrollment());
        taskRequest.setTrackedEntityInstance(rTaskRequestNonQueue.getTrackedEntityInstance());
        taskRequest.setCreateTime(rTaskRequestNonQueue.getCreateTime());
        taskRequest.setUpdateTime(rTaskRequestNonQueue.getUpdateTime());
        taskRequest.setLastSyncTime(rTaskRequestNonQueue.getLastSyncTime());
        taskRequest.setLastSyncStatus(rTaskRequestNonQueue.isLastSyncStatus());
        taskRequest.setNeedSync(rTaskRequestNonQueue.isNeedSync());
        taskRequest.setHadSynced(rTaskRequestNonQueue.isHadSynced());
        taskRequest.setLastError(rTaskRequestNonQueue.getLastError());
        return taskRequest;
    }

    public RTaskRequestNonQueue(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public RealmList<RTaskEvent> getEventList() {
        return eventList;
    }

    public void setEventList(RealmList<RTaskEvent> eventList) {
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

    public void setTrackedEntityInstance(RTaskTrackedEntityInstance trackedEntityInstance) {
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

    public boolean isNeedSync() {
        return needSync;
    }

    public void setNeedSync(boolean needSync) {
        this.needSync = needSync;
    }

    public boolean isHadSynced() {
        return hadSynced;
    }

    public void setHadSynced(boolean hadSynced) {
        this.hadSynced = hadSynced;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
}
