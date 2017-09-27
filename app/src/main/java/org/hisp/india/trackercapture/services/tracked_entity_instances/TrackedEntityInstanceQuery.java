package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.trackercapture.models.storage.RAttribute;
import org.hisp.india.trackercapture.models.storage.RTaskAttribute;
import org.hisp.india.trackercapture.models.storage.RTaskTrackedEntityInstance;
import org.hisp.india.trackercapture.models.storage.RTrackedEntityInstance;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class TrackedEntityInstanceQuery {

    public static List<RTrackedEntityInstance> getTrackedEntityInstances(String orgId, String programId) {
        return RealmHelper.query(realm -> {
            RealmResults<RTrackedEntityInstance> tei = realm.where(RTrackedEntityInstance.class)
                    .equalTo("orgUnitId", orgId)
                    .equalTo("programId", programId)
                    .findAll();

            HashMap<String, Boolean> teiKeys = new HashMap<>();
            for (RTrackedEntityInstance rTrackedEntityInstance : tei) {
                if (rTrackedEntityInstance.getTrackedEntityInstanceId() != null) {
                    teiKeys.put(rTrackedEntityInstance.getTrackedEntityInstanceId(), true);
                }
            }
            List<RTrackedEntityInstance> res = realm.copyFromRealm(tei);

            //Get TEI from Task Queue
            RealmResults<RTaskTrackedEntityInstance> teiInQueue = realm.where(RTaskTrackedEntityInstance.class)
                    .equalTo("orgUnitId", orgId)
                    .equalTo("programId", programId)
                    .findAll();

            for (RTaskTrackedEntityInstance rTaskTrackedEntityInstance : teiInQueue) {
                RTrackedEntityInstance trackedEntityInstance = new RTrackedEntityInstance();

                RealmList<RAttribute> attributes = new RealmList<>();
                for (RTaskAttribute rTaskAttribute : rTaskTrackedEntityInstance.getAttributeRequestList()) {
                    RAttribute att = new RAttribute();
                    att.setDisplayName(rTaskAttribute.getDisplayName());
                    att.setAttributeId(rTaskAttribute.getAttributeId());
                    att.setValue(rTaskAttribute.getValue());
                    att.setValueType(rTaskAttribute.getValueType());
                    attributes.add(att);
                }
                trackedEntityInstance.setAttributeList(attributes);
                trackedEntityInstance.setOrgUnitId(rTaskTrackedEntityInstance.getOrgUnitId());
                trackedEntityInstance.setProgramId(rTaskTrackedEntityInstance.getProgramId());
                trackedEntityInstance.setTrackedEntityId(rTaskTrackedEntityInstance.getTrackedEntityId());
                trackedEntityInstance.setTrackedEntityInstanceId(rTaskTrackedEntityInstance.getTrackedEntityInstanceId());

                if (trackedEntityInstance.getTrackedEntityInstanceId() == null ||
                        !teiKeys.containsKey(trackedEntityInstance.getTrackedEntityInstanceId())) {
                    res.add(trackedEntityInstance);
                }
            }

            return res;
        });
    }

    public static void insertOrUpdate(List<RTrackedEntityInstance> trackedEntityInstanceList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(trackedEntityInstanceList));
    }

}
