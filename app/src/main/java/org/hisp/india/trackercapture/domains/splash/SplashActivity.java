package org.hisp.india.trackercapture.domains.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.login.LoginActivity_;
import org.hisp.india.trackercapture.domains.main.MainActivity_;
import org.hisp.india.trackercapture.utils.AppUtils;

import javax.inject.Inject;

/**
 * Created by nhancao on 4/6/17.
 */

@EActivity(R.layout.activity_splash)
public class SplashActivity extends MvpActivity<SplashView, SplashPresenter> implements SplashView {

    @ViewById(R.id.activity_splash_background)
    RelativeLayout rlBackground;
    @ViewById(R.id.activity_splash_iv_logo)
    ImageView ivLogo;

    @App
    MainApplication application;
    @Inject
    SplashPresenter presenter;

    @AfterInject
    void inject() {
        DaggerSplashComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .build()
                .inject(this);
    }

    @AfterViews
    void init() {
        scaleUp();
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void goToMain() {
        MainActivity_.intent(this).start();
        finish();
    }

    @Override
    public void goToLogin() {
        LoginActivity_.intent(SplashActivity.this).start();
        finish();
    }

    private void scaleUp() {

        ObjectAnimator animTranslationY = ObjectAnimator.ofFloat(ivLogo, "translationY", AppUtils.convertDpToPixels(153, this), 0);
        animTranslationY.setDuration(700);

        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(ivLogo, "alpha", 0, 1);
        animAlpha.setDuration(500);

        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 0.45f, 1.05f);
        animScaleX.setDuration(1000);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 0.45f, 1.05f);
        animScaleY.setDuration(1000);

        ObjectAnimator animSlightBounceX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 1.05f, 1);
        animSlightBounceX.setStartDelay(1000);
        animSlightBounceX.setDuration(600);
        ObjectAnimator animSlightBounceY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 1.05f, 1);
        animSlightBounceY.setStartDelay(1000);
        animSlightBounceY.setDuration(600);

        final AnimatorSet animationSet = new AnimatorSet();
        animationSet.playTogether(animScaleX, animScaleY, animAlpha, animTranslationY, animSlightBounceX, animSlightBounceY);
        animationSet.start();
        animationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scaleDown();
            }
        });
    }

    private void scaleDown() {
        //Hide logo
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 1f, 0.6f);
        animScaleX.setDuration(1000);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 1f, 0.6f);
        animScaleY.setDuration(1000);

        ObjectAnimator animTranslationY = ObjectAnimator.ofFloat(ivLogo, "translationY", AppUtils.convertDpToPixels(-173, this));
        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(rlBackground, "alpha", 1, 0);
        animAlpha.setDuration(1000);

        final AnimatorSet animationSet = new AnimatorSet();
        animationSet.playTogether(animScaleX, animScaleY, animTranslationY, animAlpha);
        animationSet.start();
        animationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                presenter.gotoNextScreen();
            }
        });

    }

}