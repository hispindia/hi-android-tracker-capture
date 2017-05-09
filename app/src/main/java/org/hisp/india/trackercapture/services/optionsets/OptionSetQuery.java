package org.hisp.india.trackercapture.services.optionsets;

import org.hisp.india.trackercapture.models.storage.ROptionSet;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class OptionSetQuery {

    public static List<ROptionSet> getAllOptionSets() {
        return RealmHelper.query(realm -> {
            RealmResults<ROptionSet> programs = realm.where(ROptionSet.class).findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<ROptionSet> optionSetList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(optionSetList));
    }

}
