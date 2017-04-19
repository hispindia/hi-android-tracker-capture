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
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NToolbar;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
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
    @Inject
    NavigatorHolder navigatorHolder;

    private Navigator navigator = new SupportFragmentNavigator(getSupportFragmentManager(), R.id.main_container) {
        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            return Step1Fragment.getNewInstance((int) data);
        }

        @Override
        protected void showSystemMessage(String message) {
            Toast.makeText(EnrollActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void exit() {
            finish();
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
                finish();
            }

            @Override
            public void toolbarSettingClick() {

            }
        });
        //Update other things here
        navigator.applyCommand(new Replace(Screens.SAMPLE_SCREEN, 1));

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @NonNull
    @Override
    public EnrollPresenter createPresenter() {
        return presenter;
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
