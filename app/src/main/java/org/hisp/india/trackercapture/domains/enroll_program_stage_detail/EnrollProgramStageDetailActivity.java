package org.hisp.india.trackercapture.domains.enroll_program_stage_detail;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
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
import org.hisp.india.trackercapture.domains.enroll_program_stage.EnrollProgramStageActivity;
import org.hisp.india.trackercapture.models.base.StageDetail;
import org.hisp.india.trackercapture.models.e_num.ProgramStatus;
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.models.storage.RProgramStageDataElement;
import org.hisp.india.trackercapture.models.tmp.TMEnrollProgram;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.DatePickerDialog;
import org.hisp.india.trackercapture.widgets.NToolbar;

import java.util.List;

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
    @ViewById(R.id.fragment_enroll_program_stage_detail_cb_status)
    protected CheckBox cbStatus;

    @App
    protected MainApplication application;
    @Extra
    protected String tmEnrollProgramJson;
    protected TMEnrollProgram tmEnrollProgram;
    @Extra
    protected String programStageStr;
    @Inject
    protected EnrollProgramStageDetailPresenter presenter;
    private RProgramStage programStage;
    private EnrollProgramStageDetailAdapter adapter;

    private Navigator navigator = command -> {
        if (command instanceof Back) {
            Intent intent = getIntent();

            StageDetail stageDetail = new StageDetail(programStage.getId(),
                                                      tvDueDateValue.getText().toString(),
                                                      tvReportDateValue.getText().toString(),
                                                      adapter.getProgramStageDataElementList());
            intent.putExtra(EnrollProgramStageActivity.ENROLL_REQUEST_DATA, stageDetail.toString());
            setResult(EnrollProgramStageActivity.ENROLL_REQUEST_CODE, intent);
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
        programStage = RProgramStage.fromJson(programStageStr);

        adapter = new EnrollProgramStageDetailAdapter(this);

        lvStage.setAdapter(adapter);
        lvStage.post(() -> presenter.getProgramStageDetail(programStage.getProgramStageDataElements()));

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
    public void getProgramStageDetail(List<RProgramStageDataElement> programStageDataElements) {
        if (programStageDataElements == null) {
            Toast.makeText(application, "Program stage detail is null", Toast.LENGTH_SHORT).show();
        } else {
            //Report part
            cbStatus.setChecked(programStage.getStatus().equals(ProgramStatus.COMPLETED.name()));

            tvDueDateValue.setText(programStage.getDueDate());
            tvReportDateValue.setText(programStage.getEventDate());

            adapter.setProgramStageDataElementList(programStageDataElements);

            lvStage.post(() -> AppUtils.refreshListViewAsNonScroll(lvStage));
            vRoot.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.fragment_enroll_program_stage_detail_tv_due_date_value)
    protected void tvDueDateValueClick() {
        DatePickerDialog
                datePicker = DatePickerDialog
                .newInstance(true);
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            tvDueDateValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(getSupportFragmentManager());

    }

    @Click(R.id.fragment_enroll_program_stage_detail_tv_report_date_value)
    protected void tvReportDateValueClick() {
        DatePickerDialog
                datePicker = DatePickerDialog
                .newInstance(false);
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            tvReportDateValue.setText(AppUtils.getDateFormatted(year, month + 1, dayOfMonth));
        });
        datePicker.show(getSupportFragmentManager());

    }

}
