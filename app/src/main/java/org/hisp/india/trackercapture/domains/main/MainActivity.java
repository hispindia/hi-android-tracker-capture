package org.hisp.india.trackercapture.domains.main;

import android.support.annotation.NonNull;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.utils.AppUtil;
import org.hisp.india.trackercapture.widgets.NToolbar;

import javax.inject.Inject;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView {

    @ViewById(R.id.activity_main_toolbar)
    NToolbar toolbar;

    @App
    MainApplication application;
    @Inject
    MainPresenter presenter;

    @AfterInject
    void inject() {
        DaggerMainComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .build()
                .inject(this);
    }

    @AfterViews
    void init() {
        //Making notification bar transparent
        AppUtil.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applyMainUi(this, "Main", new NToolbar.MainItemClick() {
            @Override
            public void toolbarIconClick() {

            }

            @Override
            public void toolbarProfileClick() {

            }
        });
        //Update other things here

    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return presenter;
    }

}
