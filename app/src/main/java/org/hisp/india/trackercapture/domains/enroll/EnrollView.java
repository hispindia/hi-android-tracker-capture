package org.hisp.india.trackercapture.domains.enroll;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import ru.terrakok.cicerone.Navigator;

/**
 * Created by nhancao on 5/5/17.
 */

public interface EnrollView extends MvpView {

    Navigator getNavigator();
    /**
     * Display a loading view while loading data in background.
     */
    void showLoading();

    /**
     * Display a loading view while loading data in background.
     */
    void hideLoading();
}
