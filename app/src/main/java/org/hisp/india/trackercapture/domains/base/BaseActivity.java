package org.hisp.india.trackercapture.domains.base;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.androidannotations.annotations.EActivity;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.widgets.RotateLoading;

import java.lang.ref.WeakReference;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by nhancao on 5/5/17.
 */
@EActivity
public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> {

    private Dialog progressDialogLoading;
    private WeakReference<CircleProgressBar> circleProgressBarRef;
    private WeakReference<RotateLoading> rotateloadingRef;
    private WeakReference<TextView> tvMessageRef;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showProgressLoading() {
        showProgressLoading(null);
    }

    public void setProgressCount(int progressCount) {
        if (circleProgressBarRef != null && circleProgressBarRef.get() != null) {
            if (rotateloadingRef != null && rotateloadingRef.get() != null) {
                rotateloadingRef.get().setVisibility(View.GONE);
            }
            circleProgressBarRef.get().setVisibility(View.VISIBLE);
            circleProgressBarRef.get().setProgress(Math.min(100, Math.max(0, progressCount)));
        }
    }

    public void enableCircleProgressView(boolean enable) {
        if (enable && circleProgressBarRef != null && circleProgressBarRef.get() != null) {
            if (rotateloadingRef != null && rotateloadingRef.get() != null) {
                rotateloadingRef.get().setVisibility(View.GONE);
            }
            circleProgressBarRef.get().setVisibility(View.VISIBLE);
        } else {
            if (rotateloadingRef != null && rotateloadingRef.get() != null) {
                rotateloadingRef.get().setVisibility(View.VISIBLE);
            }
            circleProgressBarRef.get().setVisibility(View.GONE);
        }
    }

    public void updateProgressText(String message) {
        if (tvMessageRef != null && tvMessageRef.get() != null) {
            tvMessageRef.get().setText(message);
        }
    }

    public void showProgressLoading(String msg) {

        if (progressDialogLoading != null) {
            progressDialogLoading.dismiss();
        }
        View view = getLayoutInflater().inflate(R.layout.layout_progress_loading, null);
        CircleProgressBar circleProgressBar = (CircleProgressBar) view
                .findViewById(R.id.layout_loading_circle_progress);
        RotateLoading rotateloading = (RotateLoading) view.findViewById(R.id.layout_loading_rotate);
        TextView tvMessage = (TextView) view.findViewById(R.id.layout_loading_tv_msg);
        if (TextUtils.isEmpty(msg)) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(msg);
        }

        circleProgressBar.setVisibility(View.GONE);
        circleProgressBarRef = new WeakReference<>(circleProgressBar);
        rotateloadingRef = new WeakReference<>(rotateloading);
        tvMessageRef = new WeakReference<>(tvMessage);

        rotateloading.start();
        progressDialogLoading = new Dialog(this);
        progressDialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialogLoading.setContentView(view);
        progressDialogLoading.setCancelable(false);
        progressDialogLoading.setCanceledOnTouchOutside(false);

        Window window = progressDialogLoading.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.bg_layout_loading);
        }
        if (!isFinishing()) {
            progressDialogLoading.show();
        }
    }

    public void hideProgressLoading() {
        if (progressDialogLoading != null && progressDialogLoading.isShowing()) {
            progressDialogLoading.dismiss();
        }
    }

}
