package org.hisp.india.trackercapture.domains.enroll_program_stage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.domains.enroll_program_stage_detail.EnrollProgramStageDetailActivity_;
import org.hisp.india.trackercapture.models.base.StageDetail;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;
import org.hisp.india.trackercapture.models.tmp.TMEnrollProgram;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NToolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Forward;

@EActivity(R.layout.activity_enroll_program_stage)
public class EnrollProgramStageActivity extends BaseActivity<EnrollProgramStageView, EnrollProgramStagePresenter>
        implements EnrollProgramStageView {
    public static final int ENROLL_REQUEST_CODE = 1;
    public static final String ENROLL_REQUEST_DATA = "ENROLL_REQUEST_DATA";
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
    protected String tmEnrollProgramJson;
    protected TMEnrollProgram tmEnrollProgram;

    @Inject
    protected EnrollProgramStagePresenter presenter;

    private EnrollProgramStageAdapter adapter;

    private Navigator navigator = command -> {
        if (command instanceof Back) {
            finish();
        } else if (command instanceof Forward) {
            if (((Forward) command).getScreenKey().equals(Screens.ENROLL_PROGRAM_STAGE_DETAIL)) {

                RProgramStage programStage = (RProgramStage) ((Forward) command).getTransitionData();

                EnrollProgramStageDetailActivity_.intent(this)
                                                 .programStageStr(programStage.toString())
                                                 .startForResult(ENROLL_REQUEST_CODE);
            }
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
        tmEnrollProgram = TMEnrollProgram.fromJson(tmEnrollProgramJson);

        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applyEnrollProgramStageUi(this, "Program stages", new NToolbar.EnrollProgramStageToolbarItemClick() {
            @Override
            public void toolbarCloseClick() {
                presenter.onBackCommandClick();
            }

            @Override
            public void toolbarBackupClick() {
                //register program
                presenter.registerProgram(tmEnrollProgram.getTaskRequest().getTrackedEntityInstance(),
                                          tmEnrollProgram.getTaskRequest().getEnrollment(),
                                          getEventList(adapter.getProgramStageList()));
            }
        });

        adapter = new EnrollProgramStageAdapter();
        adapter.setItemClickListener(model -> presenter.openProgramStage(model));

        lvStage.setAdapter(adapter);
        lvStage.post(() -> {
            getProgramDetail(tmEnrollProgram.getProgram());
        });

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
    public void showLoading(String msg) {
        showProgressLoading(msg);
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
            //Enrollment part
            vIncidentDate.setVisibility(programDetail.isDisplayIncidentDate() ? View.VISIBLE : View.GONE);
            tvIncidentDateLabel.setText(programDetail.getIncidentDateLabel());
            tvEnrollmentDateLabel.setText(programDetail.getEnrollmentDateLabel());

            RTaskEnrollment enrollment = tmEnrollProgram.getTaskRequest().getEnrollment();
            tvIncidentDateValue.setText(enrollment.getIncidentDate());
            tvEnrollmentDateValue.setText(enrollment.getEnrollmentDate());

            adapter.setEnrollmentDate(enrollment.getEnrollmentDate());
            adapter.setProgramStageList(programDetail.getProgramStages());
            lvStage.post(() -> AppUtils.refreshListViewAsNonScroll(lvStage));
            vRoot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void registerProgramSuccess() {
        Toast.makeText(application, "Register program succeed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMessage(String msg) {
        Toasty.info(this, msg).show();
    }

    @CheckedChange({R.id.fragment_enroll_program_stage_cb_status})
    void cbStastusChecked(boolean isChecked) {
        if (isChecked) {
            tmEnrollProgram.getTaskRequest().getEnrollment().setStatus(ProgramStatus.COMPLETED.name());
        } else {
            tmEnrollProgram.getTaskRequest().getEnrollment().setStatus(ProgramStatus.ACTIVE.name());
        }
    }

    @OnActivityResult(ENROLL_REQUEST_CODE)
    void onResult(int resultCode, Intent data) {

        String stageDetailStr = data.getStringExtra(ENROLL_REQUEST_DATA);
        if (stageDetailStr != null) {
            StageDetail stageDetail = StageDetail.fromJson(stageDetailStr);
            adapter.updateProgramStageDataElement(stageDetail);
        }
    }

    public List<RTaskEvent> getEventList(List<RProgramStage> programStageList) {
        List<RTaskEvent> eventList = new ArrayList<>();
        for (RProgramStage rProgramStage : programStageList) {
            eventList.add(RTaskEvent.create(rProgramStage));
        }
        return eventList;
    }


}
