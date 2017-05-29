package org.hisp.india.trackercapture.domains.tracked_entity;

import android.support.annotation.NonNull;
import android.widget.ListView;

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

@EActivity(R.layout.activity_tracked_entity)
public class TrackedEntityActivity extends BaseActivity<TrackedEntityView, TrackedEntityPresenter> implements
                                                                                                   TrackedEntityView {
    private static final String TAG = TrackedEntityActivity.class.getSimpleName();

    @ViewById(R.id.activity_tracked_entity_toolbar)
    protected NToolbar toolbar;
    @ViewById(R.id.fragment_tracked_entity_lv_profile)
    protected ListView lvItem;

    @App
    protected MainApplication application;
    @Extra
    protected RowModel rowModel;
    @Inject
    protected TrackedEntityPresenter presenter;

    private TrackedEntityAdapter adapter;


    private Navigator navigator = command -> {
        if (command instanceof Back) {
            finish();
        }
    };

    @AfterInject
    void inject() {
        DaggerTrackedEntityComponent.builder()
                                    .applicationComponent(application.getApplicationComponent())
                                    .build()
                                    .inject(this);
    }

    @AfterViews
    void init() {
        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        toolbar.applyEnrollProgramUi(this, "Tracked entity", () -> presenter.onBackCommandClick());

        adapter = new TrackedEntityAdapter();
        lvItem.setAdapter(adapter);

        lvItem.post(() -> {
            adapter.setQueryResponse(rowModel);
        });

    }

    @NonNull
    @Override
    public TrackedEntityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }


}
