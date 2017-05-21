package org.hisp.india.trackercapture.domains.enroll_program;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.domains.enroll_program_stage.EnrollProgramStageActivity_;
import org.hisp.india.trackercapture.models.request.AttributeRequest;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NToolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Forward;

@EActivity(R.layout.activity_enroll_program)
public class EnrollProgramActivity extends BaseActivity<EnrollProgramView, EnrollProgramPresenter> implements
                                                                                                   EnrollProgramView {
    private static final String TAG = EnrollProgramActivity.class.getSimpleName();

    @ViewById(R.id.activity_enroll_program_toolbar)
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
    @ViewById(R.id.activity_main_root_scroll)
    protected View vRoot;

    @App
    protected MainApplication application;
    @Extra
    protected String organizationUnitId;
    @Extra
    protected String programId;
    @Extra
    protected String programName;
    @Inject
    protected EnrollProgramPresenter presenter;

    private EnrollProgramAdapter adapter;
    private RProgram programDetail;

    private EnrollmentRequest enrollmentRequest;
    private TrackedEntityInstanceRequest trackedEntityInstanceRequest;


    private Navigator navigator = command -> {
        if (command instanceof Back) {
            finish();
        } else if (command instanceof Forward) {
            if (((Forward) command).getScreenKey().equals(Screens.ENROLL_PROGRAM_STAGE)) {
                finish();
                EnrollProgramStageActivity_.intent(this)
                                           .organizationUnitId(organizationUnitId)
                                           .programId(programId)
                                           .programName(programName)
                                           .enrollmentRequest(enrollmentRequest)
                                           .trackedEntityInstanceRequest(trackedEntityInstanceRequest)
                                           .start();
            }
        }
    };

    @AfterInject
    void inject() {
        DaggerEnrollProgramComponent.builder()
                                    .applicationComponent(application.getApplicationComponent())
                                    .build()
                                    .inject(this);
    }

    @AfterViews
    void init() {
        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applyEnrollProgramUi(this, "Enroll", () -> presenter.onBackCommandClick());

        adapter = new EnrollProgramAdapter(this, programName);
        lvProfile.setAdapter(adapter);

        lvProfile.post(() -> presenter.getProgramDetail(programId));

    }

    @NonNull
    @Override
    public EnrollProgramPresenter createPresenter() {
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
            adapter.setProgramTrackedEntityAttributeList(programDetail.getProgramTrackedEntityAttributes());
            AppUtils.refreshListViewAsNonScroll(lvProfile);
            vRoot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void registerProgramSuccess(BaseResponse baseResponse) {
        presenter.saveProgram(programDetail);
        Toast.makeText(application, baseResponse.toString(), Toast.LENGTH_SHORT).show();
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

    @FocusChange(R.id.fragment_enroll_tv_incident_date_value)
    void tvIncidentDateValueFocus() {
        tvIncidentDateValueClick();
    }

    @FocusChange(R.id.fragment_enroll_tv_enrollment_date_value)
    void tvEnrollmentDateValueFocus() {
        tvEnrollmentDateValueClick();
    }

    @Click(R.id.activity_login_bt_register)
    void btRegisterClick() {

        boolean checkForm = true;
        List<AttributeRequest> attributeRequestList = new ArrayList<>();
        for (RProgramTrackedEntityAttribute programTrackedEntityAttribute : adapter
                .getProgramTrackedEntityAttributeList()) {
            if (!TextUtils.isEmpty(programTrackedEntityAttribute.getValue())) {
                attributeRequestList
                        .add(new AttributeRequest(programTrackedEntityAttribute.getTrackedEntityAttribute().getId(),
                                                  programTrackedEntityAttribute.getValue()));
            } else if (programTrackedEntityAttribute.isMandatory()) {
                checkForm = false;
            }
        }

        if (checkForm) {
            trackedEntityInstanceRequest =
                    new TrackedEntityInstanceRequest(programDetail.getTrackedEntity().getId(),
                                                     organizationUnitId,
                                                     attributeRequestList);

            enrollmentRequest = new EnrollmentRequest(programId,
                                                      organizationUnitId,
                                                      tvEnrollmentDateValue.getText().toString(),
                                                      tvIncidentDateValue.getText().toString());
            presenter.gotoProgramStage();
//            presenter.registerProgram(trackedEntityInstanceRequest, enrollmentRequest);
        } else {
            enrollmentRequest = null;
            trackedEntityInstanceRequest = null;

            Toast.makeText(application, "Required fields are missing.", Toast.LENGTH_SHORT).show();
        }

    }
}
