package org.hisp.india.trackercapture.services.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.di.ApplicationComponent;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;

/**
 * Created by nhancao on 8/26/17.
 */

public class AutoSyncService extends IntentService {
    public static final int SYNCING = 0;
    public static final int FINISH = 1;
    private static final String TAG = AutoSyncService.class.getSimpleName();
    public static int SYNC_STATUS = FINISH;

    public static EventBus bus = new EventBus();
    private NetworkProvider networkProvider;
    private SyncService syncService;
    private ApplicationComponent applicationComponent;
    private SyncCallback<RTaskRequest> syncCallback = new SyncCallback<RTaskRequest>() {
        @Override
        public void succeed(RTaskRequest item) {
            postBus(new SyncBus(SyncBus.State.SUCCESS, item));
            syncService.syncUnResolveEnrollProgram(syncCallback);
        }

        @Override
        public void error(String e) {
            Log.e(TAG, "error: " + e);
            SYNC_STATUS = FINISH;
            postBus(new SyncBus(SyncBus.State.ERROR, e));
        }
    };

    public AutoSyncService() {
        this(TAG);
    }

    public AutoSyncService(String name) {
        super(name);
    }

    public static void start(Context context) {
        context.startService(new Intent(context, AutoSyncService.class));
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initComponent();
        if (networkProvider.isNetworkAvailable()) {
            SYNC_STATUS = SYNCING;
            syncService.syncUnResolveEnrollProgram(syncCallback);
        } else {
            SYNC_STATUS = FINISH;
            postBus(new SyncBus(SyncBus.State.ERROR, "No network"));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    private void initComponent() {
        if (applicationComponent == null) {
            applicationComponent = ((MainApplication) getApplication()).getApplicationComponent();
            syncService = applicationComponent.syncService();
            networkProvider = applicationComponent.networkProvider();
        }
    }

    private void postBus(SyncBus busProgress) {
        RxScheduler.runOnUi(o -> bus.post(busProgress));
    }
}