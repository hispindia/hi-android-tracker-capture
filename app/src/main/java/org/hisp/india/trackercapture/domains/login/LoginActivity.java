package org.hisp.india.trackercapture.domains.login;

import android.support.annotation.NonNull;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;

import javax.inject.Inject;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {

    @App
    MainApplication application;
    @Inject
    LoginPresenter presenter;

    @AfterInject
    void inject() {
        DaggerLoginComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .build()
                .inject(this);
    }

    @AfterViews
    void init() {


    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return presenter;
    }

}
