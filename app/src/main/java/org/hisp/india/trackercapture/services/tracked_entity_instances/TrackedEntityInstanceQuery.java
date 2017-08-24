package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.trackercapture.models.storage.RTrackedEntityInstance;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class TrackedEntityInstanceQuery {

    public static List<RTrackedEntityInstance> getTrackedEntityInstances(String orgId, String programId) {
        return RealmHelper.query(realm -> {
            RealmResults<RTrackedEntityInstance> programs = realm.where(RTrackedEntityInstance.class)
                    .equalTo("orgUnitId", orgId)
                    .equalTo("programId", programId)
                    .findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<RTrackedEntityInstance> trackedEntityInstanceList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(trackedEntityInstanceList));
    }

}
