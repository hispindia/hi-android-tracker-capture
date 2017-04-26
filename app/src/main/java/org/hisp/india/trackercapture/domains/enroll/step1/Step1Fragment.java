package org.hisp.india.trackercapture.domains.enroll.step1;

import android.util.Log;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.enroll.EnrollActivity;
import org.hisp.india.trackercapture.models.base.BaseModel;
import org.hisp.india.trackercapture.models.base.Model;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.option.OptionDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by nhancao on 4/20/17.
 */

@EFragment(R.layout.fragment_step1)
public class Step1Fragment extends MvpFragment<Step1View, Step1Presenter> implements Step1View {
    private static final String TAG = Step1Fragment.class.getSimpleName();

    @ViewById(R.id.fragment_step1_tv_date_of_birth)
    TextView tvDateOfBirth;
    @ViewById(R.id.fragment_step1_tv_enroll_date)
    TextView tvEnrollDate;
    @ViewById(R.id.fragment_step1_tv_gender)
    TextView tvGender;

    @App
    MainApplication application;
    @Inject
    Step1Presenter presenter;

    private EnrollActivity activity;

    public static Step1Fragment getNewInstance() {
        return new Step1Fragment_();
    }

    @AfterInject
    void inject() {
        DaggerStep1Component.builder()
                            .applicationComponent(application.getApplicationComponent())
                            .build()
                            .inject(this);
    }

    @AfterViews
    void init() {
        activity = (EnrollActivity) getActivity();
    }

    @Override
    public Step1Presenter createPresenter() {
        return presenter;
    }


    @Override
    public void showLoading() {
        activity.showProgressLoading();
    }

    @Override
    public void hideLoading() {
        activity.hideProgressLoading();
    }

    @Click(R.id.fragment_step1_tv_enroll_date)
    void tvEnrollDateClick() {
        DatePickerDialog datePicker = DatePickerDialog.newInstance(false);
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            Log.e(TAG, "tvEnrollDateClick: " + year + " " + month + " " + dayOfMonth);
            tvEnrollDate.setText(AppUtils.getDateFormatted(year, month, dayOfMonth));
        });
        datePicker.show(activity.getSupportFragmentManager());
    }

    @Click(R.id.fragment_step1_tv_date_of_birth)
    void tvDateOfBirthClick() {
        DatePickerDialog datePicker = DatePickerDialog.newInstance(false);
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            Log.e(TAG, "tvDateOfBirthClick: " + year + " " + month + " " + dayOfMonth);
            tvDateOfBirth.setText(AppUtils.getDateFormatted(year, month, dayOfMonth));
        });
        datePicker.show(activity.getSupportFragmentManager());
    }

    @Click(R.id.fragment_step1_tv_gender)
    void tvGenderClick() {
        List<Model> modelList = new ArrayList<Model>() {
            {
                add(new BaseModel("0", "Male"));
                add(new BaseModel("1", "Female"));
            }
        };
        OptionDialog.newInstance(modelList, model -> {
            Log.e(TAG, "tvGenderClick: " + model);
            tvGender.setText(model.getDisplayName());
        }).show(activity.getSupportFragmentManager());
    }
}
