package org.hisp.india.trackercapture.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by nhancao on 8/27/17.
 */

public class NDialog {

    public static AlertDialog.Builder newAlertBuilder(Context activityContext, String message) {
        return newAlertBuilder(activityContext, message, -1, null);
    }

    public static AlertDialog.Builder newAlertBuilder(Context activityContext, @LayoutRes int resource, SetupView initView) {
        return newAlertBuilder(activityContext, null, resource, initView);
    }

    public static AlertDialog.Builder newAlertBuilder(Context activityContext, String message, @LayoutRes int resource, SetupView initView) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(activityContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(activityContext);
        }

        if (resource > -1) {
            View view = View.inflate(activityContext, resource, null);
            if (initView != null) initView.init(view);
            builder.setTitle("Info")
                    .setView(view)
                    .setIcon(android.R.drawable.ic_dialog_info);
        } else {
            builder.setTitle("Alert")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert);
        }
        return builder;

    }

    public interface SetupView {
        void init(View view);
    }
}
