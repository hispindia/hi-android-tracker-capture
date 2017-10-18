package org.hisp.india.trackercapture.services.RTaskRequestNonQueue;

import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RTaskRequestNonQueue;

import java.util.List;

import rx.Observable;

/**
 * Created by Ahmed on 10/18/2017.
 */

public class DefaultRTaskRequestNonQueueService implements RTaskRequestNonQueueService {
    @Override
    public Observable<List<RTaskRequestNonQueue>> getRTaskRequestNonQueryLocal(RProgram program, ROrganizationUnit organizationUnit) {
        return Observable.defer(()->
                        Observable.just(RTaskRequestNonQueueQuery.getRTaskRequestNonQueues(program,organizationUnit)));
    }
}
