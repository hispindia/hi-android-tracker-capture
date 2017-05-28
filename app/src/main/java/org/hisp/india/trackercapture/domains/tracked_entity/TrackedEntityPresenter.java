package org.hisp.india.trackercapture.domains.tracked_entity;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 5/5/17.
 */

public class TrackedEntityPresenter extends MvpBasePresenter<TrackedEntityView> {
    private static final String TAG = TrackedEntityPresenter.class.getSimpleName();

    private Router router;
    private NavigatorHolder navigatorHolder;

    @Inject
    public TrackedEntityPresenter(Router router, NavigatorHolder navigatorHolder) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
    }

    @Override
    public void attachView(TrackedEntityView view) {
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
