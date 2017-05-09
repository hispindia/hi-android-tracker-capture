package org.hisp.india.trackercapture.widgets;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.util.Calendar;

/**
 * Created by nhancao on 4/26/17.
 */

public class DatePickerDialog extends DialogFragment {
    private static final String TAG = DatePickerDialog.class.getSimpleName();
    private static final String ARG_ALLOW_DATES_IN_FUTURE = "arg:allowDatesInFuture";

    @Nullable
    private android.app.DatePickerDialog.OnDateSetListener onDateSetListener;

    public static DatePickerDialog newInstance(boolean allowDatesInFuture) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(ARG_ALLOW_DATES_IN_FUTURE, allowDatesInFuture);

        DatePickerDialog fragment = new DatePickerDialog();
        fragment.setArguments(arguments);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                getContext(), onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
        }
        if (!isAllowDatesInFuture()) {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        return datePickerDialog;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    public void setOnDateSetListener(
            @Nullable
                    android.app.DatePickerDialog.OnDateSetListener listener) {
        this.onDateSetListener = listener;
    }

    private boolean isAllowDatesInFuture() {
        return getArguments().getBoolean(ARG_ALLOW_DATES_IN_FUTURE, false);
    }

}