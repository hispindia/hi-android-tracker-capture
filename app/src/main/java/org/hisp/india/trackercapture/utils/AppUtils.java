package org.hisp.india.trackercapture.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by nhancao on 4/5/17.
 */

public class AppUtils {

    /**
     * Convert dp to pixel
     *
     * @param dp
     * @param context
     * @return
     */
    public static int convertDpToPixels(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * A method to find height of the status bar
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Making notification bar transparent
     */
    public static void changeStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Apply animation on height of view
     *
     * @param view
     * @param from
     * @param to
     * @param time
     */
    public static void animationHeight(final View view, int from, int to, int time) {
        ValueAnimator va = ValueAnimator.ofInt(from, to);
        va.setDuration(time);
        va.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            if (view != null) {
                view.getLayoutParams().height = value.intValue();
                view.requestLayout();
            }
        });
        va.start();
    }
}
