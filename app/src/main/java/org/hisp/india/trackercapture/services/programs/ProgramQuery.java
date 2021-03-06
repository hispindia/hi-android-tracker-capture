package org.hisp.india.trackercapture.services.programs;

import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.utils.RealmHelper;

/**
 * Created by nhancao on 5/7/17.
 */

public class ProgramQuery {

    public static RProgram getProgram(String programId) {
        return RealmHelper.query(realm -> {
            RProgram program = realm.where(RProgram.class).equalTo("id", programId).findFirst();
            if (program != null) {
                return realm.copyFromRealm(program);
            }
            return null;
        });
    }

    public static RProgram getProgramByName(String name) {
        return RealmHelper.query(realm -> {
            RProgram program = realm.where(RProgram.class).equalTo("displayName", name).findFirst();
            if (program != null) {
                return realm.copyFromRealm(program);
            }
            return null;
        });

    }

    public static RProgramStage getProgramStage(String programStageId) {
        return RealmHelper.query(realm -> {
            RProgramStage program = realm.where(RProgramStage.class).equalTo("id", programStageId).findFirst();
            if (program != null) {
                return realm.copyFromRealm(program);
            }
            return null;
        });
    }

    public static void saveProgram(RProgram program) {
        RealmHelper.transaction(realm -> {
            realm.insertOrUpdate(program);
        });
    }

}
