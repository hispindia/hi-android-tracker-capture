package org.hisp.india.trackercapture.services.sync;

import org.hisp.india.trackercapture.models.e_num.SyncKey;
import org.hisp.india.trackercapture.models.storage.RSync;

/**
 * Created by nhancao on 7/11/17.
 */

public interface SyncService {

    RSync getSyncRowByKey(SyncKey syncKey);

}
