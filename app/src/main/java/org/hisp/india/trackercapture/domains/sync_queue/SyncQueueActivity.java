package org.hisp.india.trackercapture.domains.sync_queue;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;
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
import org.hisp.india.trackercapture.models.base.RowModel;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.tmp.TMEnrollProgram;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.sync.SyncQuery;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NToolbar;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Forward;

@EActivity(R.layout.activity_sync_queue)
public class SyncQueueActivity extends BaseActivity<SyncQueueView, SyncQueuePresenter> implements
                                                                                       SyncQueueView {
    private static final String TAG = SyncQueueActivity.class.getSimpleName();

    @ViewById(R.id.activity_sync_queue_toolbar)
    protected NToolbar toolbar;
    @ViewById(R.id.activity_sync_queue_lv_profile)
    protected ListView lvItem;

    @App
    protected MainApplication application;
    @Extra
    protected RowModel rowModel;

    @Inject
    protected SyncQueuePresenter presenter;

    private SyncQueueAdapter adapter;

    private Navigator navigator = command -> {
        if (command instanceof Back) {
            finish();
        } else if (command instanceof Forward) {
            if (((Forward) command).getScreenKey().equals(Screens.ENROLL_PROGRAM_STAGE)) {

                RTaskRequest taskRequest = SyncQuery.getRTaskRequest((String) ((Forward) command).getTransitionData());
                RTaskEnrollment enrollment = taskRequest.getEnrollment();
                String orgUnitId = null, programId = null;
                if (enrollment != null) {
                    orgUnitId = enrollment.getOrgUnitId();
                    programId = enrollment.getProgramId();
                }

                if (orgUnitId != null && programId != null) {
                    finish();

                    EnrollProgramStageActivity_.intent(this)
                                               .tmEnrollProgramJson(
                                                       TMEnrollProgram
                                                               .toJson(new TMEnrollProgram(taskRequest)))
                                               .start();
                } else {
                    Toast.makeText(application, "TaskRequest info is null", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @AfterInject
    void inject() {
        DaggerSyncQueueComponent.builder()
                                .applicationComponent(application.getApplicationComponent())
                                .build()
                                .inject(this);
    }

    @AfterViews
    void init() {
        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applySyncQueueUi(this, "Sync Queue", () -> presenter.onBackCommandClick());

        adapter = new SyncQueueAdapter(new SyncQueueAdapter.SyncQueueAdapterCallback() {
            @Override
            public void removeTask(RTaskRequest rTask) {
                adapter.removeTask(rTask);
            }

            @Override
            public void onClick(RTaskRequest rTask) {
                SyncQueueDialog.newInstance(rTask)
                               .setDialogInterface(new SyncQueueDialog.DialogInterface() {
                                   @Override
                                   public void onSyncClick(DialogFragment dialogFragment, String taskId) {
                                       presenter.syncProgram(taskId);
                                   }

                                   @Override
                                   public void onEditClick(DialogFragment dialogFragment, String taskId) {
                                       presenter.editProgram(taskId);
                                   }
                               }).show(getSupportFragmentManager());
            }
        });
        lvItem.setAdapter(adapter);
        updateTaskList();

    }

    @NonNull
    @Override
    public SyncQueuePresenter createPresenter() {
        return presenter;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void syncSucceed() {
        Toast.makeText(application, "Sync succeed", Toast.LENGTH_SHORT).show();
        updateTaskList();
    }

    @Override
    public void syncError(String e) {
        Toast.makeText(application, "Sync error: " + e, Toast.LENGTH_SHORT).show();
        updateTaskList();
    }

    private void updateTaskList() {lvItem.post(() -> adapter.setTaskList(SyncQuery.getRTaskRequestList()));}

}
