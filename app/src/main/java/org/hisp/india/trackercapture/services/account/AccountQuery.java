package org.hisp.india.trackercapture.services.account;

import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.utils.RealmHelper;

/**
 * Created by nhancao on 5/4/17.
 */

public class AccountQuery {

    public static RUser getUser() {
        return RealmHelper.query(realm -> {
            RUser user = realm.where(RUser.class).findFirst();
            if (user != null) {
                return realm.copyFromRealm(user);
            }
            return null;
        });
    }


}
