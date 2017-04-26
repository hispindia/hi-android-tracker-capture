package org.hisp.india.trackercapture.services.program_rules;

import org.hisp.india.trackercapture.models.storage.RProgramRule;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class ProgramRuleModel {

    public static List<RProgramRule> getAllProgramRules() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<RProgramRule> programs = realm.where(RProgramRule.class).findAll();
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

    public static void insertOrUpdate(List<RProgramRule> programList) {
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
