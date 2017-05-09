package org.hisp.india.trackercapture.services.program_rule_actions;

import org.hisp.india.trackercapture.models.storage.RProgramRuleAction;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class ProgramRuleActionQuery {

    public static List<RProgramRuleAction> getAllProgramRuleActions() {
        return RealmHelper.query(realm -> {
            RealmResults<RProgramRuleAction> programs = realm.where(RProgramRuleAction.class).findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<RProgramRuleAction> programRuleActionList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(programRuleActionList));
    }

}
