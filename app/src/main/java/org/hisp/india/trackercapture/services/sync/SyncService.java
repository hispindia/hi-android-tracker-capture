package org.hisp.india.trackercapture.services.sync;

import org.hisp.india.trackercapture.models.storage.RTaskRequest;

/**
 * Created by nhancao on 7/11/17.
 */

public interface SyncService {

    void syncUnResolveEnrollProgram(SyncCallback<RTaskRequest> syncCallback);

    void syncEnrollProgram(String taskId, SyncCallback<RTaskRequest> syncCallback);
}
