package org.hisp.india.trackercapture.services.sync;

/**
 * Created by nhancao on 8/20/17.
 */

public interface SyncCallback<T> {

    void succeed(T item);

    void error(String e);


}
