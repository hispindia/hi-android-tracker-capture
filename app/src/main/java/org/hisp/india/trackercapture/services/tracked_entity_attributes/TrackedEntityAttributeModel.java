package org.hisp.india.trackercapture.services.tracked_entity_attributes;

import org.hisp.india.trackercapture.models.storage.RTrackedEntityAttribute;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class TrackedEntityAttributeModel {

    public static List<RTrackedEntityAttribute> getAllTrackedEntityAttributes() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<RTrackedEntityAttribute> programs = realm.where(RTrackedEntityAttribute.class).findAll();
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

    public static void insertOrUpdate(List<RTrackedEntityAttribute> programList) {
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
