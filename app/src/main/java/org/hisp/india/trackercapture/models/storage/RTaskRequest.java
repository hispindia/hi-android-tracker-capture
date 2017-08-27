package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.utils.RealmHelper;
import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 8/17/17.
 */

public class RTaskRequest extends RealmObject {

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

    public RTaskRequest() {
        this.eventList = new RealmList<>();

        setUuid(UUID.randomUUID().toString());
        setCreateTime(new DateTime().toString());
        setNeedSync(true);
    }

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
        this.eventList.clear();
        this.eventList.addAll(eventList);
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

    public void save() {
        setUpdateTime(new DateTime().toString());
        RealmHelper.transaction(realm -> {
            realm.insertOrUpdate(this);
        });
    }

    public void delete() {
        RealmHelper.transaction(realm -> {
            RTaskRequest rTask = realm.where(RTaskRequest.class).equalTo("uuid", getUuid()).findFirst();
            if (rTask != null) {
                rTask.deleteFromRealm();
            }
        });
    }

    public void updateSyncStatus(boolean isSucceed, String errorMsg) {
        setLastSyncStatus(isSucceed);
        setNeedSync(!isSucceed);
        if (!isHadSynced() && isSucceed) setHadSynced(isSucceed);
        setLastSyncTime(new DateTime().toString());
        setLastError(errorMsg);
    }

    @Override
    public String toString() {
        return "Uuid: " + uuid + '\n' +
                "Create time: " + createTime + '\n' +
                "Update time: " + updateTime + '\n' +
                "Last sync time: " + lastSyncTime + '\n' +
                "Last sync status: " + lastSyncStatus + '\n' +
                "Need sync: " + needSync + '\n' +
                "Had synced: " + hadSynced + '\n' +
                "Last error: " + lastError;
    }
}
