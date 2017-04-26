package org.hisp.india.trackercapture.services.tracked_entity_attribute_groups;

import org.hisp.india.trackercapture.models.storage.RTrackedEntityAttributeGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class TrackedEntityAttributeGroupModel {

    public static List<RTrackedEntityAttributeGroup> getAllTrackedEntityAttributeGroups() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<RTrackedEntityAttributeGroup> programs = realm.where(RTrackedEntityAttributeGroup.class)
                                                                       .findAll();
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

    public static void insertOrUpdate(List<RTrackedEntityAttributeGroup> programList) {
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
