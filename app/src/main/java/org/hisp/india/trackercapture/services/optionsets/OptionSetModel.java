package org.hisp.india.trackercapture.services.optionsets;

import org.hisp.india.trackercapture.models.storage.ROptionSet;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class OptionSetModel {

    public static List<ROptionSet> getAllOptionSets() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<ROptionSet> programs = realm.where(ROptionSet.class).findAll();
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

    public static void insertOrUpdate(List<ROptionSet> programList) {
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
