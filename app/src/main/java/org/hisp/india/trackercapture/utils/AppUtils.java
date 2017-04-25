package org.hisp.india.trackercapture.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.Normalizer;

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

    /**
     * Check is contain text
     *
     * @return true if "originalText" contain "search"
     */
    public static boolean isContainText(String search, String originalText) {
        return isContainText(search, originalText, false);
    }

    /**
     * Check is contain text
     */
    public static boolean isContainText(String search, String originalText, boolean caseSensitive) {
        if (search != null && !search.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD)
                                              .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            if (!caseSensitive) normalizedText = normalizedText.toLowerCase();
            int start = normalizedText.indexOf((!caseSensitive) ? search.toLowerCase() : search);
            if (start < 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Highlight text
     *
     * @return CharSequence had been high lighted
     */
    public static CharSequence highlightText(String search, String originalText) {
        return highlightText(search, originalText, false);
    }

    /**
     * Highlight text
     */
    public static CharSequence highlightText(String search, String originalText, boolean caseSensitive) {
        if (search != null && !search.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD)
                                              .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            if (!caseSensitive) normalizedText = normalizedText.toLowerCase();
            int start = normalizedText.indexOf((!caseSensitive) ? search.toLowerCase() : search);
            if (start < 0) {
                return originalText;
            } else {
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + search.length(), originalText.length());
                    highlighted.setSpan(new ForegroundColorSpan(Color.WHITE), spanStart, spanEnd,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(search, spanEnd);
                }
                return highlighted;
            }
        }
        return originalText;
    }


    public static DateTime parseDate(String pattern, String date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.parseDateTime(date);
    }

    public static String formatDate(String pattern, DateTime date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(date.withZone(DateTimeZone.UTC));
    }

    public static String getDateFormatted(int year, int month, int dayOfMonth) {
        return AppUtils.formatDate("yyyy-mm-dd",
                                   new DateTime().withYear(year).withMonthOfYear(month).withDayOfMonth(dayOfMonth));
    }

}
