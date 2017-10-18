package org.hisp.india.trackercapture.services.RTaskRequestNonQueue;

import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.storage.RTaskRequestNonQueue;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Ahmed on 10/16/2017.
 */

public class RTaskRequestNonQueueQuery {

    public static void save(RTaskRequestNonQueue taskRequest){
        RealmHelper.transaction(realm->{
            realm.insertOrUpdate(taskRequest);
        });
    }

    public static RTaskRequestNonQueue getRTaskRequestNonQueue(String uuid){
        return RealmHelper.query(realm->{
            RTaskRequestNonQueue taskRequestNonQueue = realm.where(RTaskRequestNonQueue.class)
                    .equalTo("uuid",uuid)
                    .findFirst();
            if(taskRequestNonQueue!=null){
                return realm.copyFromRealm(taskRequestNonQueue);
            }
            return null;
        });
    }

    public static List<RTaskRequestNonQueue> getRTaskRequestNonQueues(RProgram program, ROrganizationUnit organizationUnit){
        return RealmHelper.query(realm->{
            RealmResults<RTaskRequestNonQueue> taskRequestNonQueue = realm.where(RTaskRequestNonQueue.class)
                    .equalTo("enrollment.orgUnitId",organizationUnit.getId())
                    .equalTo("enrollment.programId",program.getId())
                    .findAll();
            if(taskRequestNonQueue!=null){
                return realm.copyFromRealm(taskRequestNonQueue);
            }
            return null;
        });
    }

    public static void clear(){
        RealmHelper.transaction(realm->realm.where(RTaskRequest.class).findAll().deleteAllFromRealm());
        //RealmHelper.transaction(realm -> realm.where(RTaskRequestNonQueue.class).findAll().deleteAllFromRealm());

    }

}
