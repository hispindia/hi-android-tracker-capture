package org.hisp.india.trackercapture.domains.enroll.step1;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by nhancao on 4/20/17.
 */

public interface Step1View extends MvpView {

    /**
     * Display a loading view while loading data in background.
     */
    void showLoading();

    /**
     * Display a loading view while loading data in background.
     */
    void hideLoading();

}