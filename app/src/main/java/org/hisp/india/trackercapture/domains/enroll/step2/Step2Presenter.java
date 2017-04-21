package org.hisp.india.trackercapture.domains.enroll.step2;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 4/20/17.
 */

public class Step2Presenter extends MvpBasePresenter<Step2View> {

    private Router router;

    @Inject
    public Step2Presenter(Router router) {
        this.router = router;
    }
}
