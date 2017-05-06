package org.hisp.india.trackercapture.domains.enroll;

import android.support.annotation.NonNull;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.models.base.BaseModel;
import org.hisp.india.trackercapture.models.base.Model;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NToolbar;
import org.hisp.india.trackercapture.widgets.option.OptionDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;

@EActivity(R.layout.activity_enroll)
public class EnrollActivity extends BaseActivity<EnrollView, EnrollPresenter> implements EnrollView {
    private static final String TAG = EnrollActivity.class.getSimpleName();

    @ViewById(R.id.activity_enroll_toolbar)
    protected NToolbar toolbar;
    @ViewById(R.id.fragment_enroll_tv_date_of_birth)
    protected TextView tvDateOfBirth;
    @ViewById(R.id.fragment_enroll_tv_enroll_date)
    protected TextView tvEnrollDate;
    @ViewById(R.id.fragment_enroll_tv_gender)
    protected TextView tvGender;

    @App
    protected MainApplication application;
    @Extra
    protected String organizationUnitId;
    @Extra
    protected String programId;
    @Inject
    protected EnrollPresenter presenter;

    private Navigator navigator = command -> {
        if (command instanceof Back) {
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
                presenter.onBackCommandClick();
            }

            @Override
            public void toolbarSettingClick() {

            }
        });


    }

    @NonNull
    @Override
    public EnrollPresenter createPresenter() {
        return presenter;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void showLoading() {
        showProgressLoading();
    }

    @Override
    public void hideLoading() {
        hideProgressLoading();
    }

    @Click(R.id.fragment_enroll_tv_enroll_date)
    void tvEnrollDateClick() {
        DatePickerDialog datePicker = DatePickerDialog.newInstance(false);
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            tvEnrollDate.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(getSupportFragmentManager());
    }

    @Click(R.id.fragment_enroll_tv_date_of_birth)
    void tvDateOfBirthClick() {
        DatePickerDialog datePicker = DatePickerDialog.newInstance(false);
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            tvDateOfBirth.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(getSupportFragmentManager());
    }

    @Click(R.id.fragment_enroll_tv_gender)
    void tvGenderClick() {
        List<Model> modelList = new ArrayList<Model>() {
            {
                add(new BaseModel("0", "Male"));
                add(new BaseModel("1", "Female"));
            }
        };
        OptionDialog.newInstance(modelList, model -> {
            tvGender.setText(model.getDisplayName());
        }).show(getSupportFragmentManager());
    }


}
