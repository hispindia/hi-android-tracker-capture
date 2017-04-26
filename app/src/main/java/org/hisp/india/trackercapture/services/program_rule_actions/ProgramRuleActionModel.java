package org.hisp.india.trackercapture.services.program_rule_actions;

import org.hisp.india.trackercapture.models.storage.RProgramRuleAction;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class ProgramRuleActionModel {

    public static List<RProgramRuleAction> getAllProgramRuleActions() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<RProgramRuleAction> programs = realm.where(RProgramRuleAction.class).findAll();
            return realm.copyFromRealm(programs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

        return new ArrayList<>();
    }

    public static void insertOrUpdate(List<RProgramRuleAction> programList) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.insertOrUpdate(programList);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

}
