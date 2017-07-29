package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.e_num.SyncKey;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 7/11/17.
 */

public class RSync extends RealmObject {
    @PrimaryKey
    private String syncKey;
    private Date updateTime;
    private boolean status;

    public static RSync create(SyncKey syncKey, boolean status) {
        RSync sync = new RSync();
        sync.setSyncKey(syncKey);
        sync.setUpdateTime(new Date());
        sync.setStatus(status);
        return sync;
    }

    public SyncKey getSyncKey() {
        return SyncKey.getType(syncKey);
    }

    public void setSyncKey(SyncKey syncKey) {
        this.syncKey = syncKey.name();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
