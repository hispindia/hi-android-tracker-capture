package org.hisp.india.trackercapture.services.RTaskRequestNonQueue;

import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RTaskRequestNonQueue;

import java.util.List;

import rx.Observable;

/**
 * Created by Ahmed on 10/18/2017.
 */

public interface RTaskRequestNonQueueService {
    Observable<List<RTaskRequestNonQueue>> getRTaskRequestNonQueryLocal(RProgram program, ROrganizationUnit organizationUnit);
}
