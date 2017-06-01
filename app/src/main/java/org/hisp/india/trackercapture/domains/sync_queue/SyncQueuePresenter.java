package org.hisp.india.trackercapture.domains.sync_queue;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.hisp.india.trackercapture.services.task.BusProgress;
import org.hisp.india.trackercapture.services.task.TaskService;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 5/5/17.
 */

public class SyncQueuePresenter extends MvpBasePresenter<SyncQueueView> {
    private static final String TAG = SyncQueuePresenter.class.getSimpleName();

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
        TaskService.bus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        TaskService.bus.unregister(this);
        super.detachView(retainInstance);
    }

    @Subscribe
    public void taskBusSubscribe(BusProgress busProgress) {
        if (isViewAttached()) {
            switch (busProgress) {
                case UP_QUEUE:
                    if (isViewAttached()) {
                        getView().updateSyncQueue();
                    }
                    break;
            }
        }
    }

    public void onBackCommandClick() {
        router.exit();
    }

}
