package org.hisp.india.trackercapture.services.program_rule_variables;

import org.hisp.india.trackercapture.models.storage.RProgramRuleVariable;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class ProgramRuleVariableQuery {

    public static List<RProgramRuleVariable> getAllProgramRuleVariables() {
        return RealmHelper.query(realm -> {
            RealmResults<RProgramRuleVariable> programs = realm.where(RProgramRuleVariable.class).findAll();
            return realm.copyFromRealm(programs);
        });
    }

    public static void insertOrUpdate(List<RProgramRuleVariable> programRuleVariableList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(programRuleVariableList));
    }

}
