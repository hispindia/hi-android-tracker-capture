package org.hisp.india.trackercapture.domains.sync_queue;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import ru.terrakok.cicerone.Navigator;

/**
 * Created by nhancao on 5/5/17.
 */

public interface SyncQueueView extends MvpView {

    Navigator getNavigator();

    void updateSyncQueue();
}
