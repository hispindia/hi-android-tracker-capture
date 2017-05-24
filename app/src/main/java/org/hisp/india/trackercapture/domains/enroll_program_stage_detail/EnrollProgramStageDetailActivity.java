package org.hisp.india.trackercapture.domains.enroll_program_stage_detail;

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
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NToolbar;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;

@EActivity(R.layout.activity_enroll_program_stage_detail)
public class EnrollProgramStageDetailActivity
        extends BaseActivity<EnrollProgramStageDetailView, EnrollProgramStageDetailPresenter>
        implements
        EnrollProgramStageDetailView {
    private static final String TAG = EnrollProgramStageDetailActivity.class.getSimpleName();

    @ViewById(R.id.activity_enroll_program_stage_toolbar)
    protected NToolbar toolbar;
    @ViewById(R.id.fragment_enroll_program_stage_detail_due_date)
    protected View vDueDate;
    @ViewById(R.id.fragment_enroll_program_stage_detail_report_date)
    protected View vReportDate;
    @ViewById(R.id.fragment_enroll_program_stage_detail_tv_due_date_label)
    protected TextView tvDueDateLabel;
    @ViewById(R.id.fragment_enroll_program_stage_detail_tv_due_date_value)
    protected TextView tvDueDateValue;
    @ViewById(R.id.fragment_enroll_program_stage_detail_tv_report_date_label)
    protected TextView tvReportDateLabel;
    @ViewById(R.id.fragment_enroll_program_stage_detail_tv_report_date_value)
    protected TextView tvReportDateValue;
    @ViewById(R.id.fragment_enroll_program_stage_detail_lv_stage)
    protected ListView lvStage;
    @ViewById(R.id.activity_main_root_scroll)
    protected View vRoot;

    @App
    protected MainApplication application;
    @Extra
    protected String programStageId;

    @Inject
    protected EnrollProgramStageDetailPresenter presenter;

    private EnrollProgramStageDetailAdapter adapter;

    private Navigator navigator = command -> {
        if (command instanceof Back) {
            finish();
        }
    };

    @AfterInject
    void inject() {
        DaggerEnrollProgramStageDetailComponent.builder()
                                               .applicationComponent(application.getApplicationComponent())
                                               .build()
                                               .inject(this);
    }

    @AfterViews
    void init() {
        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applyEnrollProgramStageDetailUi(this, "Program stages",
                                                () -> presenter.onBackCommandClick());

        adapter = new EnrollProgramStageDetailAdapter(this);

        lvStage.setAdapter(adapter);

        lvStage.post(() -> presenter.getProgramStageDetail(programStageId));

    }

    @NonNull
    @Override
    public EnrollProgramStageDetailPresenter createPresenter() {
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
    public void getProgramStageDetail(RProgramStage programStageDetail) {
        if (programStageDetail == null) {
            Toast.makeText(application, "Program stage detail is null", Toast.LENGTH_SHORT).show();
        } else {
            //Report part
            tvDueDateValue.setText(programStageDetail.getDueDate());
            tvReportDateValue.setText(programStageDetail.getEventDate());

            adapter.setProgramStageDataElementList(programStageDetail.getProgramStageDataElements());

            lvStage.post(() -> AppUtils.refreshListViewAsNonScroll(lvStage));
            vRoot.setVisibility(View.VISIBLE);
        }
    }

}
