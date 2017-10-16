package org.hisp.india.trackercapture.services.RTaskRequestNonQueue;

import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.storage.RTaskRequestNonQueue;
import org.hisp.india.trackercapture.utils.RealmHelper;

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

    public static void clear(){
        RealmHelper.transaction(realm->realm.where(RTaskRequest.class).findAll().deleteAllFromRealm());
        RealmHelper.transaction(realm -> realm.where(RTaskRequestNonQueue.class).findAll().deleteAllFromRealm());

    }

}
