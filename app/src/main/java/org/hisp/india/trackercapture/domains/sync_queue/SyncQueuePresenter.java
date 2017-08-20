package org.hisp.india.trackercapture.domains.sync_queue;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.services.sync.SyncCallback;
import org.hisp.india.trackercapture.services.sync.SyncService;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 5/5/17.
 */

public class SyncQueuePresenter extends MvpBasePresenter<SyncQueueView> {
    private static final String TAG = SyncQueuePresenter.class.getSimpleName();

    @Inject
    protected SyncService syncService;

    private Router router;
    private NavigatorHolder navigatorHolder;

    @Inject
    public SyncQueuePresenter(Router router, NavigatorHolder navigatorHolder) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
    }

    @Override
    public void attachView(SyncQueueView view) {
        super.attachView(view);
        navigatorHolder.setNavigator(view.getNavigator());
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        super.detachView(retainInstance);
    }

    public void syncProgram(String taskId) {
        syncService.syncEnrollProgram(taskId, new SyncCallback<RTaskRequest>() {
            @Override
            public void succeed(RTaskRequest item) {
                Log.e(TAG, "succeed: " + item.getUuid());
                item.updateSyncStatus(true, null);
                item.save();

                if (isViewAttached()) {
                    getView().syncSucceed();
                }
            }

            @Override
            public void error(RTaskRequest item, String e) {
                Log.e(TAG, "error: " + e);
                item.updateSyncStatus(false, e);
                if (isViewAttached()) {
                    getView().syncError(e);
                }
            }
        });
    }

    public void onBackCommandClick() {
        router.exit();
    }

}
