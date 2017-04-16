package org.hisp.india.trackercapture.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by nhancao on 4/5/17.
 */

public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    /**
     * Convert dp to pixel
     */
    public static int convertDpToPixels(float dp, Context context) {
        return (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * A method to find height of the status bar
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

    /**
     * Export database to sdcard for backup and debug
     */
    public static void exportDatabase(Context context, String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//files//" + databaseName;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, databaseName);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.e(TAG, "exportDatabase: Db file has been backup on sdcard");
                } else {
                    Log.e(TAG, "exportDatabase: current db not exists");
                }
            } else {
                Log.e(TAG, "exportDatabase: Sdcard can not Write");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
