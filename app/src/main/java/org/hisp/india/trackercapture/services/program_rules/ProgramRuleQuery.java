package org.hisp.india.trackercapture.services.program_rules;

import org.hisp.india.trackercapture.models.storage.RProgramRule;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class ProgramRuleQuery {

    public static List<RProgramRule> getAllProgramRules() {
        return RealmHelper.query(realm -> {
            RealmResults<RProgramRule> programs = realm.where(RProgramRule.class).findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<RProgramRule> programRuleList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(programRuleList));
    }

}
