package org.hisp.india.trackercapture.domains.enroll;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.hisp.india.trackercapture.models.e_num.ValueType;
import org.hisp.india.trackercapture.models.request.AttributeRequest;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.storage.ROption;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NTextChange;
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

    private QuickAdapter<RProgramTrackedEntityAttribute> adapter;
    private RProgram programDetail;

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

        adapter = new QuickAdapter<RProgramTrackedEntityAttribute>(this, R.layout.item_enroll_profile) {
            @Override
            protected void convert(BaseAdapterHelper helper, RProgramTrackedEntityAttribute item) {
                TextView tvLabel = helper.getView(R.id.item_enroll_profile_tv_label);
                TextView tvMandatory = helper.getView(R.id.item_enroll_profile_tv_mandatory);
                EditText etValue = helper.getView(R.id.item_enroll_profile_et_value);
                TextView tvValue = helper.getView(R.id.item_enroll_profile_tv_value);

                tvLabel.setText(item.getDisplayName().replace(programDetail.getDisplayName() + " ", ""));
                tvMandatory.setVisibility(item.isMandatory() ? View.VISIBLE : View.GONE);

                if (item.getTrackedEntityAttribute().isOptionSetValue()
                    || item.getValueType() == ValueType.YES_NO
                    || item.getValueType() == ValueType.YES_ONLY
                    || item.getValueType() == ValueType.DATE
                        ) {
                    tvValue.setVisibility(View.VISIBLE);
                    etValue.setVisibility(View.GONE);

                    Log.e(TAG, "convertawertasdfsadf: item.getValue " + item.getValue());
                    tvValue.setText(item.getValue());
                    tvValue.addTextChangedListener(new NTextChange(new NTextChange.AbsTextListener() {
                        @Override
                        public void after(Editable editable) {
                            item.setValue(editable.toString());
                            Log.e(TAG, "afterawesrsdfasdf: " + item.getValue());
                        }
                    }));
                } else {
                    etValue.setVisibility(View.VISIBLE);
                    tvValue.setVisibility(View.GONE);

                    Log.e(TAG, "convert: item.getValue " + item.getValue());
                    etValue.setText(item.getValue());
                    etValue.addTextChangedListener(new NTextChange(new NTextChange.AbsTextListener() {
                        @Override
                        public void after(Editable editable) {
                            item.setValue(editable.toString());
                            Log.e(TAG, "after: " + item.getValue());
                        }
                    }));
                }

                if (item.getTrackedEntityAttribute().isOptionSetValue()) {
                    tvValue.setOnClickListener(v -> {
                        List<Model> modelList = new ArrayList<>();
                        for (ROption option : item.getTrackedEntityAttribute().getOptionSet().getOptions()) {
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
                            tvValue.setOnClickListener(v -> {
                                DatePickerDialog datePicker = DatePickerDialog
                                        .newInstance(item.isAllowFutureDate());
                                datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                                    tvValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
                                });
                                datePicker.show(getSupportFragmentManager());
                            });
                            break;
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

        presenter.getProgramDetail(programId);

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
    public void getProgramDetail(RProgram programDetail) {
        if (programDetail == null) {
            Toast.makeText(application, "Program detail is null", Toast.LENGTH_SHORT).show();
        } else {
            this.programDetail = programDetail;
            //Build form
            //Enrollment part
            vIncidentDate.setVisibility(programDetail.isDisplayIncidentDate() ? View.VISIBLE : View.GONE);
            tvIncidentDateLabel.setText(programDetail.getIncidentDateLabel());
            tvEnrollmentDateLabel.setText(programDetail.getEnrollmentDateLabel());

            //Profile part
            adapter.replaceAll(programDetail.getProgramTrackedEntityAttributes());

        }
    }

    @Click(R.id.fragment_enroll_tv_incident_date_value)
    void tvIncidentDateValueClick() {
        DatePickerDialog datePicker = DatePickerDialog.newInstance(programDetail.isSelectIncidentDatesInFuture());
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            tvIncidentDateValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(getSupportFragmentManager());
    }

    @Click(R.id.fragment_enroll_tv_enrollment_date_value)
    void tvEnrollmentDateValueClick() {
        DatePickerDialog datePicker = DatePickerDialog.newInstance(programDetail.isSelectEnrollmentDatesInFuture());
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            tvEnrollmentDateValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(getSupportFragmentManager());
    }

    @Click(R.id.activity_login_bt_register)
    void btRegisterClick() {
        List<AttributeRequest> attributeRequestList = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            RProgramTrackedEntityAttribute trackedEntityAttribute = adapter.getItem(i);
            attributeRequestList
                    .add(new AttributeRequest(trackedEntityAttribute.getId(), trackedEntityAttribute.getValue()));
            Log.e(TAG, "btRegisterClick: " + trackedEntityAttribute.getValue());
        }
        TrackedEntityInstanceRequest trackedEntityInstanceRequest =
                new TrackedEntityInstanceRequest(programDetail.getTrackedEntity(),
                                                 organizationUnitId,
                                                 attributeRequestList);

        EnrollmentRequest enrollmentRequest = new EnrollmentRequest(programId,
                                                                    "ACTIVE",
                                                                    organizationUnitId,
                                                                    tvEnrollmentDateValue.getText().toString(),
                                                                    tvIncidentDateValue.getText().toString());

        presenter.registerProgram(trackedEntityInstanceRequest, enrollmentRequest);

    }
}
