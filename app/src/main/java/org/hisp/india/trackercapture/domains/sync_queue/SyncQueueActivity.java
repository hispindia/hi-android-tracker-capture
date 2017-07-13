package org.hisp.india.trackercapture.domains.sync_queue;

import android.support.annotation.NonNull;
import android.widget.ListView;

import com.nhancv.ntask.NTaskManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.models.base.RowModel;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.widgets.NToolbar;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;

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

        adapter = new SyncQueueAdapter(NTaskManager::completeTask);
        lvItem.setAdapter(adapter);

        updateSyncQueue();

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
    public void updateSyncQueue() {
        lvItem.post(() -> adapter.setTaskList(NTaskManager.getInstance().getTaskList()));
    }


}
