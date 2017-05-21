package org.hisp.india.trackercapture.domains.enroll_program_stage;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NToolbar;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;

@EActivity(R.layout.activity_enroll_program_stage)
public class EnrollProgramStageActivity extends BaseActivity<EnrollProgramStageView, EnrollProgramStagePresenter>
        implements
        EnrollProgramStageView {
    private static final String TAG = EnrollProgramStageActivity.class.getSimpleName();

    @ViewById(R.id.activity_enroll_program_stage_toolbar)
    protected NToolbar toolbar;
    @ViewById(R.id.fragment_enroll_program_stage_incident_date)
    protected View vIncidentDate;
    @ViewById(R.id.fragment_enroll_program_stage_enrollment_date)
    protected View vEnrollmentDate;
    @ViewById(R.id.fragment_enroll_program_stage_tv_incident_date_label)
    protected TextView tvIncidentDateLabel;
    @ViewById(R.id.fragment_enroll_program_stage_tv_incident_date_value)
    protected TextView tvIncidentDateValue;
    @ViewById(R.id.fragment_enroll_program_stage_tv_enrollment_date_label)
    protected TextView tvEnrollmentDateLabel;
    @ViewById(R.id.fragment_enroll_program_stage_tv_enrollment_date_value)
    protected TextView tvEnrollmentDateValue;
    @ViewById(R.id.fragment_enroll_program_stage_lv_stage)
    protected ListView lvStage;
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
    @Extra
    protected EnrollmentRequest enrollmentRequest;
    @Extra
    protected TrackedEntityInstanceRequest trackedEntityInstanceRequest;
    @Inject
    protected EnrollProgramStagePresenter presenter;

    private RProgram programDetail;
    private EnrollProgramStageAdapter adapter;

    private Navigator navigator = command -> {
        if (command instanceof Back) {
            finish();
        }
    };

    @AfterInject
    void inject() {
        DaggerEnrollProgramStageComponent.builder()
                                         .applicationComponent(application.getApplicationComponent())
                                         .build()
                                         .inject(this);
    }

    @AfterViews
    void init() {
        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applyEnrollProgramStagelUi(this, "Program stages", new NToolbar.EnrollProgramStageToolbarItemClick() {
            @Override
            public void toolbarCloseClick() {
                presenter.onBackCommandClick();
            }

            @Override
            public void toolbarBackupClick() {
                //register program
                presenter.registerProgram(trackedEntityInstanceRequest, enrollmentRequest);
            }
        });

        adapter = new EnrollProgramStageAdapter(programName);
        lvStage.setAdapter(adapter);

        lvStage.post(() -> presenter.getProgramDetail(programId));

    }

    @NonNull
    @Override
    public EnrollProgramStagePresenter createPresenter() {
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
            //Enrollment part
            vIncidentDate.setVisibility(programDetail.isDisplayIncidentDate() ? View.VISIBLE : View.GONE);
            tvIncidentDateLabel.setText(programDetail.getIncidentDateLabel());
            tvEnrollmentDateLabel.setText(programDetail.getEnrollmentDateLabel());

            tvIncidentDateValue.setText(enrollmentRequest.getIncidentDate());
            tvEnrollmentDateValue.setText(enrollmentRequest.getEnrollmentDate());

            adapter.setProgramStageList(programDetail.getProgramStages());
            lvStage.post(() -> AppUtils.refreshListViewAsNonScroll(lvStage));
            vRoot.setVisibility(View.VISIBLE);
        }
    }

}
