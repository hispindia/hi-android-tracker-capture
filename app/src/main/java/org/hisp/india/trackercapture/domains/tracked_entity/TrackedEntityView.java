package org.hisp.india.trackercapture.domains.tracked_entity;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import ru.terrakok.cicerone.Navigator;

/**
 * Created by nhancao on 5/5/17.
 */

public interface TrackedEntityView extends MvpView {

    Navigator getNavigator();

}
