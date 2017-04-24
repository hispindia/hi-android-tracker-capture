package org.hisp.india.trackercapture.domains.enroll.step2;

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

@EFragment(R.layout.fragment_step2)
public class Step2Fragment extends MvpFragment<Step2View, Step2Presenter> implements Step2View {

    @App
    MainApplication application;
    @Inject
    Step2Presenter presenter;

    public static Step2Fragment getNewInstance() {
        return new Step2Fragment_();
    }

    @AfterInject
    void inject() {
        DaggerStep2Component.builder()
                            .applicationComponent(application.getApplicationComponent())
                            .build()
                            .inject(this);
    }

    @Override
    public Step2Presenter createPresenter() {
        return presenter;
    }


}
