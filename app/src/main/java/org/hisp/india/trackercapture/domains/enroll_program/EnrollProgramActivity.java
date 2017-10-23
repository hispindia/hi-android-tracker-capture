package org.hisp.india.trackercapture.domains.enroll_program;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import org.hisp.india.trackercapture.domains.enroll_program_stage.EnrollProgramStageActivity_;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.models.storage.RTaskAttribute;
import org.hisp.india.trackercapture.models.tmp.TMEnrollProgram;
import org.hisp.india.trackercapture.models.tmp.TMItem;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;
import org.hisp.india.trackercapture.utils.AppUtils;
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
    @ViewById(R.id.fragment_enroll_rv_profile)
    protected RecyclerView rvProfile;
    @ViewById(R.id.activity_main_root_scroll)
    protected View vRoot;

    @App
    protected MainApplication application;
    @Extra
    protected String fromScreenName;
    @Extra //added by ifhaam
    protected String tmEnrollProgramJson;
    protected TMEnrollProgram tmEnrollProgram;
    @Extra
    protected boolean toRegisterNew;
    @Extra
    protected String orgUnitJson;
    @Inject
    protected EnrollProgramPresenter presenter;

    private EnrollProgramAdapter adapter;
    private RProgram programDetail;

    private Navigator navigator = command -> {
        if (command instanceof Back) {
            finish();
        } else if (command instanceof Forward) {
            if (((Forward) command).getScreenKey().equals(Screens.ENROLL_PROGRAM_STAGE)) {
                finish();

                EnrollProgramStageActivity_.intent(this)
                        .tmEnrollProgramJson(TMEnrollProgram.toJson(tmEnrollProgram))
                        .organizationUnitJson(orgUnitJson)
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
        tmEnrollProgram = TMEnrollProgram.fromJson(tmEnrollProgramJson);

        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applyEnrollProgramUi(this, "Enroll", () -> {
            if (fromScreenName != null &&
                    fromScreenName.equals(Screens.ENROLL_PROGRAM_STAGE) && !toRegisterNew) {
                btRegisterClick();
            } else {
                presenter.onBackCommandClick();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvProfile.setHasFixedSize(true);
        rvProfile.setLayoutManager(llm);


        RProgram program = tmEnrollProgram.getProgram();
        adapter = new EnrollProgramAdapter(this,
                tmEnrollProgram.getOrganizationUnitId(),
                tmEnrollProgram.getProgramId(),
                program.getDisplayName(),
                this::btRegisterClick);
        rvProfile.setAdapter(adapter);
        rvProfile.post(() -> getProgramDetail(program));

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
            List<TMItem> itemModels = new ArrayList<>();
            if (programDetail.isDisplayIncidentDate()) {
                itemModels.add(TMItem.createIncidentDate(programDetail.getIncidentDateLabel(),
                        programDetail.isSelectIncidentDatesInFuture()));
            }
            itemModels.add(TMItem.createEnrollmentDate(programDetail.getEnrollmentDateLabel(),
                    programDetail.isSelectEnrollmentDatesInFuture()));

            for (RProgramTrackedEntityAttribute rProgramTrackedEntityAttribute : programDetail
                    .getProgramTrackedEntityAttributes()) {
                itemModels.add(TMItem.createRegisterFieldItem(rProgramTrackedEntityAttribute));
            }

            if (fromScreenName == null ||
                    !fromScreenName.equals(Screens.ENROLL_PROGRAM_STAGE) || toRegisterNew) {
                itemModels.add(TMItem.createRegisterButton());
            }

            rvProfile.setItemViewCacheSize(itemModels.size());
            adapter.setModelList(itemModels);
            vRoot.setVisibility(View.VISIBLE);

            adapter.populateData(tmEnrollProgram.getTaskRequest());
        }
    }

    @Override
    public void getOrganizationUnitList(List<ROrganizationUnit> organizationUnitList) {
        rvProfile.setItemViewCacheSize(organizationUnitList.size());
        vRoot.setVisibility(View.VISIBLE);
    }

    @Override
    public void registerProgramSuccess(BaseResponse baseResponse) {
        presenter.saveProgram(programDetail);
        Toast.makeText(application, baseResponse.toString(), Toast.LENGTH_SHORT).show();
    }

    void btRegisterClick() {

        boolean checkForm = true;
        List<RTaskAttribute> taskAttributeList = new ArrayList<>();
        for (RProgramTrackedEntityAttribute programTrackedEntityAttribute : adapter
                .getProgramTrackedEntityAttributeList()) {
            if (!TextUtils.isEmpty(programTrackedEntityAttribute.getValue())) {

                String displayName = programTrackedEntityAttribute.getDisplayName();
                if (displayName != null) displayName = displayName.replace(programDetail.getDisplayName() + " ", "");

                taskAttributeList
                        .add(RTaskAttribute.create(
                                programTrackedEntityAttribute.getTrackedEntityAttribute().getId(),
                                programTrackedEntityAttribute.getValue(),
                                displayName,
                                programTrackedEntityAttribute.getValueTypeStr()));
            } else if (programTrackedEntityAttribute.isMandatory()) {
                checkForm = false;
            }
        }

        if (checkForm) {
            //@nhancv TODO: 8/18/17 Generate RTask model
            tmEnrollProgram
                    .setTrackedEntityInstance(taskAttributeList)
                    .setEnrollment(adapter.getEnrollmentDateValue(),
                            adapter.getIncidentDateValue());

            presenter.gotoProgramStage();
        } else {
            Toast.makeText(application, "Required fields are missing.", Toast.LENGTH_SHORT).show();
        }

    }


    public TrackedEntityInstanceService getTrackedEntityInstanceService() {
        return presenter.getTrackedEntityInstanceService();
    }

}
