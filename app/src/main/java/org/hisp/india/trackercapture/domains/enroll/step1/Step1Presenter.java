package org.hisp.india.trackercapture.domains.enroll.step1;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 4/20/17.
 */

public class Step1Presenter extends MvpBasePresenter<Step1View> {

    private Router router;

    @Inject
    public Step1Presenter(Router router) {
        this.router = router;
    }
}
