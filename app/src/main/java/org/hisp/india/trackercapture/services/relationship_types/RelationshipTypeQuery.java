package org.hisp.india.trackercapture.services.relationship_types;

import org.hisp.india.trackercapture.models.storage.RRelationshipType;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class RelationshipTypeQuery {

    public static List<RRelationshipType> getAllRelationshipTypes() {
        return RealmHelper.query(realm -> {
            RealmResults<RRelationshipType> programs = realm.where(RRelationshipType.class).findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<RRelationshipType> relationshipTypeList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(relationshipTypeList));
    }

}
