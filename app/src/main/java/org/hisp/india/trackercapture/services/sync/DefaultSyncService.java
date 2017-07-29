package org.hisp.india.trackercapture.services.sync;

import org.hisp.india.trackercapture.models.e_num.SyncKey;
import org.hisp.india.trackercapture.models.storage.RSync;
import org.hisp.india.trackercapture.utils.RealmHelper;

/**
 * Created by nhancao on 7/11/17.
 */

public class DefaultSyncService implements SyncService {
    @Override
    public RSync getSyncRowByKey(SyncKey syncKey) {
        return RealmHelper.query(realm -> {
            RSync sync = realm.where(RSync.class).equalTo("syncKey", syncKey.name()).findFirst();
            if (sync != null) {
                return realm.copyFromRealm(sync);
            }
            return null;
        });
    }
}
