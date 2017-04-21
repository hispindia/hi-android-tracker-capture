package org.hisp.india.trackercapture.domains.enroll.step1;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;

import javax.inject.Inject;

/**
 * Created by nhancao on 4/20/17.
 */

@EFragment(R.layout.fragment_step1)
public class Step1Fragment extends MvpFragment<Step1View, Step1Presenter> implements Step1View {

    @App
    MainApplication application;
    @Inject
    Step1Presenter presenter;

    public static Step1Fragment getNewInstance() {
        return new Step1Fragment_();
    }

    @AfterInject
    void inject() {
        DaggerStep1Component.builder()
                            .applicationComponent(application.getApplicationComponent())
                            .build()
                            .inject(this);
    }

    @Override
    public Step1Presenter createPresenter() {
        return presenter;
    }


}
