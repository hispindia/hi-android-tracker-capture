package org.hisp.india.trackercapture.domains.enroll;

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
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.models.request.AttributeRequest;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NToolbar;

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
    @Extra
    protected String programName;
    @Inject
    protected EnrollPresenter presenter;

    private EnrollAdapter adapter;
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
        toolbar.applyEnrollUi(this, "Enroll", () -> presenter.onBackCommandClick());

        adapter = new EnrollAdapter(this, programName);
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
            adapter.setProgramTrackedEntityAttributeList(programDetail.getProgramTrackedEntityAttributes());

        }
    }

    @Override
    public void registerProgramSuccess(BaseResponse baseResponse) {
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
            TrackedEntityInstanceRequest trackedEntityInstanceRequest =
                    new TrackedEntityInstanceRequest(programDetail.getTrackedEntity().getId(),
                                                     organizationUnitId,
                                                     attributeRequestList);

            EnrollmentRequest enrollmentRequest = new EnrollmentRequest(programId,
                                                                        "ACTIVE",
                                                                        organizationUnitId,
                                                                        tvEnrollmentDateValue.getText().toString(),
                                                                        tvIncidentDateValue.getText().toString());

            presenter.registerProgram(trackedEntityInstanceRequest, enrollmentRequest);
        } else {
            Toast.makeText(application, "Required fields are missing.", Toast.LENGTH_SHORT).show();
        }

    }
}
