package org.hisp.india.trackercapture.services.tracked_entity_attributes;

import org.hisp.india.trackercapture.models.storage.RTrackedEntityAttribute;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class TrackedEntityAttributeQuery {

    public static List<RTrackedEntityAttribute> getAllTrackedEntityAttributes() {
        return RealmHelper.query(realm -> {
            RealmResults<RTrackedEntityAttribute> programs = realm.where(RTrackedEntityAttribute.class).findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<RTrackedEntityAttribute> trackedEntityAttributeList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(trackedEntityAttributeList));
    }

}
