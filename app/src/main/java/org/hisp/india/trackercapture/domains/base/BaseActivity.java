package org.hisp.india.trackercapture.domains.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.androidannotations.annotations.EActivity;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.widgets.RotateLoading;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by nhancao on 5/5/17.
 */
@EActivity
public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> {

    private Dialog progressDialogLoading;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showProgressLoading() {

        if (progressDialogLoading != null) {
            progressDialogLoading.dismiss();
        }
        View view = getLayoutInflater().inflate(R.layout.layout_progress_loading, null);
        RotateLoading rotateloading = (RotateLoading) view.findViewById(R.id.layout_loading_rotate);
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

        progressDialogLoading.show();
    }

    public void hideProgressLoading() {
        if (progressDialogLoading != null && progressDialogLoading.isShowing()) {
            progressDialogLoading.dismiss();
        }
    }

}
