package org.hisp.india.trackercapture.domains.enroll_program_stage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.domains.enroll_program.EnrollProgramActivity_;
import org.hisp.india.trackercapture.domains.enroll_program_stage_detail.EnrollProgramStageDetailActivity_;
import org.hisp.india.trackercapture.models.base.StageDetail;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.response.OrganizationUnitsResponse;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.tmp.TMEnrollProgram;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.organization.OrganizationQuery;
import org.hisp.india.trackercapture.services.sync.AutoSyncService;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NToolbar;
import org.hisp.india.trackercapture.widgets.autocomplete.AutocompleteDialog;
import org.w3c.dom.Text;

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
    public static final String ORG_UNIT_JSON = "ORG_UNIT_JSON";
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
    @ViewById(R.id.tv_select_program)
    protected TextView selectProgram;


    @App
    protected MainApplication application;


    @Extra
    protected String tmEnrollProgramJson;
    protected TMEnrollProgram tmEnrollProgram;
    //added by ifhaam

    protected ROrganizationUnit organizationUnit;//ends

    @Inject
    protected EnrollProgramStagePresenter presenter;

    private EnrollProgramStageAdapter adapter;

    private AutocompleteDialog dialog;

    private boolean toRegisterNew;
    ArrayList<RProgram> programs = new ArrayList<>();
    private Navigator navigator = command -> {
        if (command instanceof Back) {
            finish();
        } else if (command instanceof Forward) {
            if (((Forward) command).getScreenKey().equals(Screens.ENROLL_PROGRAM)) {
                finish();
                TMEnrollProgram enrollProgram = (TMEnrollProgram)((Forward)command).getTransitionData();
                if(enrollProgram!=null){
                    tmEnrollProgram = enrollProgram;
                }
                EnrollProgramActivity_.intent(this)
                                      .tmEnrollProgramJson(TMEnrollProgram.toJson(tmEnrollProgram))
                                      //.orgUnitJson(ROrganizationUnit.toJson(organizationUnit))
                                      .toRegisterNew(toRegisterNew)
                                      .fromScreenName(Screens.ENROLL_PROGRAM_STAGE)
                                      .start();

            } else if (((Forward) command).getScreenKey().equals(Screens.ENROLL_PROGRAM_STAGE_DETAIL)) {

                RProgramStage programStage = (RProgramStage) ((Forward) command).getTransitionData();
                EnrollProgramStageDetailActivity_.intent(this)
                                                 .programStageStr(programStage.toString())
                                                 .program(tmEnrollProgram.getProgram().getId())
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

        //added by ifhaam
        organizationUnit = OrganizationQuery.getOrganisationUnitId(tmEnrollProgram.getOrganizationUnitId());
        if(organizationUnit!=null && organizationUnit.getPrograms().size()>0){
            for(RProgram program :organizationUnit.getPrograms()) {
                programs.add(program);
            }

        }

        selectProgram.setText(tmEnrollProgram.getProgram().getDisplayName());

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
                presenter.registerProgram(tmEnrollProgram, getEventList(adapter.getProgramStageList()));
            }
        });

        adapter = new EnrollProgramStageAdapter();
        adapter.setItemClickListener(model -> {
            presenter.openProgramStage(model);
        });

        lvStage.setAdapter(adapter);
        lvStage.post(() -> getProgramDetail(tmEnrollProgram.getProgram()));

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



            adapter.populateData(tmEnrollProgram.getTaskRequest());
        }
    }

    @Override
    public void registerProgramSyncRequest(String msg) {
        Toasty.info(this, msg).show();
        AutoSyncService.start(getApplicationContext());
    }

    @CheckedChange({R.id.fragment_enroll_program_stage_cb_status})
    protected void cbStastusChecked(boolean isChecked) {
        if (isChecked) {
            tmEnrollProgram.getTaskRequest().getEnrollment().setStatus(ProgramStatus.COMPLETED.name());
        } else {
            tmEnrollProgram.getTaskRequest().getEnrollment().setStatus(ProgramStatus.ACTIVE.name());
        }
    }

    @OnActivityResult(ENROLL_REQUEST_CODE)
    protected void onResult(int resultCode, Intent data) {
        String stageDetailStr = data.getStringExtra(ENROLL_REQUEST_DATA);
        if (stageDetailStr != null) {
            StageDetail stageDetail = StageDetail.fromJson(stageDetailStr);
            adapter.updateProgramStageDataElement(stageDetail);
        }
    }

    @Click(R.id.fragment_enroll_program_stage_v_basic_info)
    protected void vBasicInfoClick() {
        navigator.applyCommand(new Forward(Screens.ENROLL_PROGRAM, null));
    }

    @Click(R.id.tv_select_program)
    protected void tvSelectProgramClick(){
        if (programs.size()>0) {
            dialog = AutocompleteDialog.newInstance(programs, (model) -> {
                if (dialog != null) {
                    dialog.dismiss();
                }
                selectProgram.setText(model.getDisplayName());
                TMEnrollProgram newEnrollProgram = new TMEnrollProgram(organizationUnit.getId(),model.getId());
                //newEnrollProgram.getTaskRequest().setTrackedEntityInstance(tmEnrollProgram.getTaskRequest().getTrackedEntityInstance());
                newEnrollProgram = newEnrollProgram.setTrackedEntityInstance(tmEnrollProgram.getTaskRequest().getTrackedEntityInstance().getAttributeRequestList());
                newEnrollProgram.getTaskRequest().getTrackedEntityInstance().setTrackedEntityInstanceId(
                        tmEnrollProgram.getTaskRequest().getTrackedEntityInstance().getTrackedEntityInstanceId()
                );
                toRegisterNew = true;
                presenter.enrollAnotherProgram(newEnrollProgram);
            });
            dialog.show(getSupportFragmentManager());
        }else{
            selectProgram.setText(tmEnrollProgram.getProgram().getDisplayName());
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
