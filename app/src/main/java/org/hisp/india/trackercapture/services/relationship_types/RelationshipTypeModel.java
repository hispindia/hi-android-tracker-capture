package org.hisp.india.trackercapture.services.relationship_types;

import org.hisp.india.trackercapture.models.storage.RRelationshipType;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class RelationshipTypeModel {

    public static List<RRelationshipType> getAllRelationshipTypes() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<RRelationshipType> programs = realm.where(RRelationshipType.class).findAll();
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

    public static void insertOrUpdate(List<RRelationshipType> programList) {
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
