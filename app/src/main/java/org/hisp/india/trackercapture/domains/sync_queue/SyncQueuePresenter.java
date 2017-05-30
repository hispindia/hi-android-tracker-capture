package org.hisp.india.trackercapture.domains.sync_queue;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

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
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        super.detachView(retainInstance);
    }

    public void onBackCommandClick() {
        router.exit();
    }

}
