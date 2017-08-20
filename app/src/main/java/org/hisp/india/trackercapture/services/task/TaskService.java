package org.hisp.india.trackercapture.services.task;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.nhancv.ntask.AbstractTaskService;
import com.nhancv.ntask.NTaskManager;

import org.greenrobot.eventbus.EventBus;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.di.ApplicationComponent;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.events.EventService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

import rx.Subscription;

/**
 * Created by nhancao on 5/23/17.
 */

public class TaskService extends AbstractTaskService {

    public static EventBus bus = new EventBus();
    private final int MAX_RETRY = 3;
    private Gson gson;
    private EventService eventService;
    private EnrollmentService enrollmentService;
    private TrackedEntityInstanceService trackedEntityInstanceService;
    private Subscription subscription;
    private ApplicationComponent applicationComponent;

    @Override
    protected void doing(Intent intent) {
        if (isNetworkAvailable()) {
            initComponent();

            doingLoop();

            checkErrorTask();
        } else {
            postBus(BusProgress.ERROR);
        }
    }

    private void checkErrorTask() {
        if (NTaskManager.hasNext()) {
            doingLoop();
        } else {
            int errorCount = (int) NTaskManager.getInstance().getCountByErrorStatus();
            if (errorCount > 0) {
                System.out.println("Failed tasks: " + errorCount);
                NTaskManager.getInstance().resetStatusQueue();
                System.out.println("Wait....");
                SystemClock.sleep(10000);
                doingLoop();
            } else {
                postBus(BusProgress.UP_QUEUE);
                System.out.println("Done");
            }
        }
    }

    private void doingLoop() {

    }

    private void initComponent() {
        if (applicationComponent == null) {
            applicationComponent = ((MainApplication) getApplication()).getApplicationComponent();
            gson = new Gson();
            eventService = applicationComponent.eventService();
            enrollmentService = applicationComponent.enrollmentService();
            trackedEntityInstanceService = applicationComponent.trackedEntityInstanceService();

        }
    }

    private void postBus(BusProgress busProgress) {
        RxScheduler.runOnUi(o -> bus.post(busProgress));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}