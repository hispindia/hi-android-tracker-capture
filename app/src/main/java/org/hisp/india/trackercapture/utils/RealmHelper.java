package org.hisp.india.trackercapture.utils;

import io.realm.Realm;

/**
 * Created by nhancao on 5/4/17.
 */

public class RealmHelper {

    public static void transaction(RealmTransaction realmTransaction) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realmTransaction.execute(realm);
            realm.commitTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public static <T> T query(RealmQuery<T> realmDoing) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            return realmDoing.query(realm);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return null;
    }

    public interface RealmTransaction {
        void execute(Realm realm);
    }

    public interface RealmQuery<T> {
        T query(Realm realm);
    }
}
