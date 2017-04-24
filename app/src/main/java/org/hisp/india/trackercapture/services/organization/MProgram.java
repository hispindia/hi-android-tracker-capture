package org.hisp.india.trackercapture.services.organization;

import org.hisp.india.trackercapture.models.storage.TProgram;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class MProgram {

    public static List<TProgram> getAllPrograms() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<TProgram> programs = realm.where(TProgram.class).findAll();
            return realm.copyFromRealm(programs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

        return new ArrayList<>();
    }

    public static void insertOrUpdate(List<TProgram> programList) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.insertOrUpdate(programList);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

}
