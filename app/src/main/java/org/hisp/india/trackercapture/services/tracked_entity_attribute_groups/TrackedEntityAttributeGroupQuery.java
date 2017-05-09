package org.hisp.india.trackercapture.services.tracked_entity_attribute_groups;

import org.hisp.india.trackercapture.models.storage.RTrackedEntityAttributeGroup;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class TrackedEntityAttributeGroupQuery {

    public static List<RTrackedEntityAttributeGroup> getAllTrackedEntityAttributeGroups() {
        return RealmHelper.query(realm -> {
            RealmResults<RTrackedEntityAttributeGroup> programs = realm.where(RTrackedEntityAttributeGroup.class)
                                                                       .findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<RTrackedEntityAttributeGroup> trackedEntityAttributeGroupList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(trackedEntityAttributeGroupList));
    }

}
