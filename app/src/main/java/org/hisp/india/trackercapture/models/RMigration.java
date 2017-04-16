package org.hisp.india.trackercapture.models;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmMigration;

/**
 * Created by nhancao on 4/16/17.
 */

public class RMigration implements RealmMigration {
    public static final int DB_VERSION = 0;

    public static void clearAll() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

    }
}
