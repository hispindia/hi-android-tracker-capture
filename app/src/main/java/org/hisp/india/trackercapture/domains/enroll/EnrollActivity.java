package org.hisp.india.trackercapture.domains.enroll;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.domains.enroll.step1.Step1Fragment;
import org.hisp.india.trackercapture.domains.enroll.step2.Step2Fragment;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NToolbar;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.Replace;

@EActivity(R.layout.activity_enroll)
public class EnrollActivity extends BaseActivity<EnrollView, EnrollPresenter> implements EnrollView {
    private static final String TAG = EnrollActivity.class.getSimpleName();

    @ViewById(R.id.activity_enroll_toolbar)
    NToolbar toolbar;

    @App
    MainApplication application;
    @Inject
    EnrollPresenter presenter;

    private Navigator navigator = new SupportFragmentNavigator(getSupportFragmentManager(), R.id.main_container) {
        private String currentScreenKey;

        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            this.currentScreenKey = screenKey;
            if (screenKey.equals(Screens.STEP1_SCREEN)) {
                return Step1Fragment.getNewInstance();
            } else if (screenKey.equals(Screens.STEP2_SCREEN)) {
                return Step2Fragment.getNewInstance();
            }
            return null;
        }

        @Override
        protected void showSystemMessage(String message) {
            Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void exit() {
            if (currentScreenKey != null && currentScreenKey.equals(Screens.STEP1_SCREEN)) {
                finish();
            } else {
                applyCommand(new Replace(Screens.STEP1_SCREEN, null));
            }
        }

    };

    @AfterInject
    void inject() {
        DaggerEnrollComponent.builder()
                             .applicationComponent(application.getApplicationComponent())
                             .build()
                             .inject(this);
    }

    @AfterViews
    void init() {
        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applyEnrollUi(this, "Enroll", new NToolbar.EnrollToolbarItemClick() {
            @Override
            public void toolbarCloseClick() {
                presenter.onBackCommandClick();
            }

            @Override
            public void toolbarSettingClick() {
                presenter.gotoStep2Fragment();
            }
        });

        presenter.gotoStep1Fragment();

    }

    @NonNull
    @Override
    public EnrollPresenter createPresenter() {
        return presenter;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void showLoading() {
        showProgressLoading();
    }

    @Override
    public void hideLoading() {
        hideProgressLoading();
    }


}
