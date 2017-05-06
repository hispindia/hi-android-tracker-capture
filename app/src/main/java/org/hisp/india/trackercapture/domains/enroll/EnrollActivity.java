package org.hisp.india.trackercapture.domains.enroll;

import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

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
import org.hisp.india.trackercapture.models.base.Option;
import org.hisp.india.trackercapture.models.base.ProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.models.e_num.ValueType;
import org.hisp.india.trackercapture.models.response.ProgramDetailResponse;
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
    @ViewById(R.id.fragment_enroll_incident_date)
    protected View vIncidentDate;
    @ViewById(R.id.fragment_enroll_enrollment_date)
    protected View vEnrollmentDate;
    @ViewById(R.id.fragment_enroll_tv_incident_date_label)
    protected TextView tvIncidentDateLabel;
    @ViewById(R.id.fragment_enroll_tv_incident_date_value)
    protected TextView tvIncidentDateValue;
    @ViewById(R.id.fragment_enroll_tv_enrollment_date_label)
    protected TextView tvEnrollmentDateLabel;
    @ViewById(R.id.fragment_enroll_tv_enrollment_date_value)
    protected TextView tvEnrollmentDateValue;
    @ViewById(R.id.fragment_enroll_lv_profile)
    protected ListView lvProfile;

    @App
    protected MainApplication application;
    @Extra
    protected String organizationUnitId;
    @Extra
    protected String programId;
    @Inject
    protected EnrollPresenter presenter;

    private QuickAdapter<ProgramTrackedEntityAttribute> adapter;
    private ProgramDetailResponse programDetailResponse;

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

        presenter.getProgramDetail(programId);

        adapter = new QuickAdapter<ProgramTrackedEntityAttribute>(this, R.layout.item_enroll_profile) {
            @Override
            protected void convert(BaseAdapterHelper helper, ProgramTrackedEntityAttribute item) {

                TextView tvLabel = helper.getView(R.id.item_enroll_profile_tv_label);
                EditText etValue = helper.getView(R.id.item_enroll_profile_et_value);
                TextView tvValue = helper.getView(R.id.item_enroll_profile_tv_value);

                tvLabel.setText(item.getDisplayName().replaceFirst(programDetailResponse.getDisplayName(), ""));

                if (item.getTrackedEntityAttribute().isOptionSetValue()
                    || item.getValueType() == ValueType.YES_NO
                    || item.getValueType() == ValueType.YES_ONLY) {
                    tvValue.setVisibility(View.VISIBLE);
                    etValue.setVisibility(View.GONE);
                } else {
                    etValue.setVisibility(View.VISIBLE);
                    tvValue.setVisibility(View.GONE);
                }

                if (item.getTrackedEntityAttribute().isOptionSetValue()) {
                    tvValue.setOnClickListener(v -> {
                        List<Model> modelList = new ArrayList<>();
                        for (Option option : item.getTrackedEntityAttribute().getOptionSet().getOptions()) {
                            modelList.add(new BaseModel(option.getId(), option.getDisplayName()));
                        }

                        OptionDialog.newInstance(modelList, model -> {
                            tvValue.setText(model.getDisplayName());
                        }).show(getSupportFragmentManager());
                    });
                } else {
                    switch (item.getValueType()) {
                        case TEXT:
                        case LONG_TEXT:
                        case LETTER:
                            etValue.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case NUMBER:
                            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                        case DATE:
                        case DATE_TIME:
                        case TIME:
                            etValue.setInputType(InputType.TYPE_CLASS_DATETIME);
                            break;
                        case PHONE_NUMBER:
                            etValue.setInputType(InputType.TYPE_CLASS_PHONE);
                            break;
                        case YES_NO:
                            tvValue.setOnClickListener(v -> {
                                List<Model> modelList = new ArrayList<Model>() {
                                    {
                                        add(new BaseModel("0", "Yes"));
                                        add(new BaseModel("1", "No"));
                                    }
                                };
                                OptionDialog.newInstance(modelList, model -> {
                                    tvValue.setText(model.getDisplayName());
                                }).show(getSupportFragmentManager());
                            });
                        case YES_ONLY:
                            tvValue.setText("Yes");
                            break;
                        default:
                            etValue.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                    }
                }

            }
        };
        lvProfile.setAdapter(adapter);

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

    @Override
    public void getProgramDetailSuccess(ProgramDetailResponse programDetailResponse) {
        this.programDetailResponse = programDetailResponse;
        //Build form
        //Enrollment part
        vIncidentDate.setVisibility(programDetailResponse.isDisplayIncidentDate() ? View.VISIBLE : View.GONE);
        tvIncidentDateLabel.setText(programDetailResponse.getIncidentDateLabel());
        tvEnrollmentDateLabel.setText(programDetailResponse.getEnrollmentDateLabel());

        //Profile part
        adapter.replaceAll(programDetailResponse.getProgramTrackedEntityAttributes());
    }

    @Click(R.id.fragment_enroll_tv_incident_date_value)
    void tvIncidentDateValueClick() {
        DatePickerDialog datePicker = DatePickerDialog.newInstance(false);
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            tvIncidentDateValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(getSupportFragmentManager());
    }

    @Click(R.id.fragment_enroll_tv_enrollment_date_value)
    void tvEnrollmentDateValueClick() {
        DatePickerDialog datePicker = DatePickerDialog.newInstance(false);
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            tvEnrollmentDateValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(getSupportFragmentManager());
    }

}
