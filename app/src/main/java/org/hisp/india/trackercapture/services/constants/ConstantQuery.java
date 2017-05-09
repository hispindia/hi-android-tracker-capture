package org.hisp.india.trackercapture.services.constants;

import org.hisp.india.trackercapture.models.storage.RConstant;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class ConstantQuery {

    public static List<RConstant> getAllConstants() {
        return RealmHelper.query(realm -> {
            RealmResults<RConstant> programs = realm.where(RConstant.class).findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<RConstant> constantList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(constantList));
    }

}
