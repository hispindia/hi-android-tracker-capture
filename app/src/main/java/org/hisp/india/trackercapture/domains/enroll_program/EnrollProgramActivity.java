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
import org.hisp.india.trackercapture.models.request.AttributeRequest;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.models.tmp.ItemModel;
import org.hisp.india.trackercapture.navigator.Screens;
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

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvProfile.setHasFixedSize(true);
        rvProfile.setLayoutManager(llm);
        adapter = new EnrollProgramAdapter(this, programName, this::btRegisterClick);
        rvProfile.setAdapter(adapter);
        rvProfile.post(() -> presenter.getProgramDetail(programId));

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
            List<ItemModel> itemModels = new ArrayList<>();
            if (programDetail.isDisplayIncidentDate()) {
                itemModels.add(ItemModel.createIncidentDate(programDetail.getIncidentDateLabel(),
                                                            programDetail.isSelectIncidentDatesInFuture()));
            }
            itemModels.add(ItemModel.createEnrollmentDate(programDetail.getEnrollmentDateLabel(),
                                                          programDetail.isSelectEnrollmentDatesInFuture()));

            for (RProgramTrackedEntityAttribute rProgramTrackedEntityAttribute : programDetail
                    .getProgramTrackedEntityAttributes()) {
                itemModels.add(ItemModel.createRegisterFieldItem(rProgramTrackedEntityAttribute));
            }
            itemModels.add(ItemModel.createRegisterButton());

            rvProfile.setItemViewCacheSize(itemModels.size());
            adapter.setModelList(itemModels);
            vRoot.setVisibility(View.VISIBLE);
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
                                                      adapter.getEnrollmentDateValue(),
                                                      adapter.getIncidentDateValue());
            presenter.gotoProgramStage();
        } else {
            enrollmentRequest = null;
            trackedEntityInstanceRequest = null;

            Toast.makeText(application, "Required fields are missing.", Toast.LENGTH_SHORT).show();
        }

    }
}
