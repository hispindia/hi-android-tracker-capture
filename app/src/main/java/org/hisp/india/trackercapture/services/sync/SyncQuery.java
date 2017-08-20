package org.hisp.india.trackercapture.services.sync;

import org.hisp.india.trackercapture.models.e_num.SyncKey;
import org.hisp.india.trackercapture.models.storage.RSync;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

/**
 * Created by nhancao on 4/24/17.
 */

public class SyncQuery {

    public static RSync getSyncRowByKey(SyncKey syncKey) {
        return RealmHelper.query(realm -> {
            RSync sync = realm.where(RSync.class).equalTo("syncKey", syncKey.name()).findFirst();
            if (sync != null) {
                return realm.copyFromRealm(sync);
            }
            return null;
        });
    }

    public static List<RTaskRequest> getRTaskRequestList() {
        return RealmHelper.query(realm -> realm.copyFromRealm(realm.where(RTaskRequest.class).findAll()));
    }

    public static RTaskRequest getRTaskRequest(String taskId) {
        return RealmHelper.query(realm -> {
            RTaskRequest rTask = realm.where(RTaskRequest.class).equalTo("uuid", taskId).findFirst();
            if (rTask != null) {
                return realm.copyFromRealm(rTask);
            }
            return null;
        });
    }

    public static void insertOrUpdateTaskEnrollment(List<RTaskRequest> rTaskRequestList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(rTaskRequestList));
    }

}
