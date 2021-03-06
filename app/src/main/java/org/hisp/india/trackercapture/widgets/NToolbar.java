package org.hisp.india.trackercapture.widgets;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.R;

/**
 * Created by nhancao on 5/5/17.
 */

@EViewGroup(R.layout.view_toolbar)
public class NToolbar extends RelativeLayout {
    private static final String TAG = NToolbar.class.getSimpleName();

    @ViewById(R.id.view_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.view_toolbar_icon)
    protected View toolbarIcon;
    @ViewById(R.id.view_toolbar_close)
    protected View toolbarClose;
    @ViewById(R.id.view_toolbar_tv_title)
    protected TextView tvTitle;
    @ViewById(R.id.view_toolbar_backup)
    protected View toolbarBackup;

    public NToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void applyCommonUi(AppCompatActivity activity) {
        //Setup toolbar
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        toolbarClose.setVisibility(View.GONE);
        toolbarBackup.setVisibility(View.GONE);
    }

    public void applyTemplatelUi(AppCompatActivity activity, String title, TemplateToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarClose.setVisibility(View.VISIBLE);

        toolbarClose.setOnClickListener(v -> itemClick.toolbarCloseClick());

    }

    public void applyEnrollProgramUi(AppCompatActivity activity, String title,
                                     DefaultToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarClose.setVisibility(View.VISIBLE);

        toolbarClose.setOnClickListener(v -> itemClick.toolbarCloseClick());

    }

    public void applyTrackedEntityUi(AppCompatActivity activity, String title,
                                     DefaultToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarClose.setVisibility(View.VISIBLE);

        toolbarClose.setOnClickListener(v -> itemClick.toolbarCloseClick());

    }

    public void applySyncQueueUi(AppCompatActivity activity, String title,
                                 SyncQueueToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarClose.setVisibility(View.VISIBLE);
        toolbarBackup.setVisibility(View.VISIBLE);

        toolbarClose.setOnClickListener(v -> itemClick.toolbarCloseClick());
        toolbarBackup.setOnClickListener(v -> itemClick.toolbarSyncAllClick());


    }

    public void applyEnrollProgramStageUi(AppCompatActivity activity, String title,
                                          EnrollProgramStageToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarClose.setVisibility(View.VISIBLE);
        toolbarBackup.setVisibility(View.VISIBLE);

        toolbarClose.setOnClickListener(v -> itemClick.toolbarCloseClick());
        toolbarBackup.setOnClickListener(v -> itemClick.toolbarBackupClick());

    }

    public void applyEnrollProgramStageDetailUi(AppCompatActivity activity, String title,
                                                DefaultToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarClose.setVisibility(View.VISIBLE);

        toolbarClose.setOnClickListener(v -> itemClick.toolbarCloseClick());

    }

    public void setEnableLeftButton(boolean enable) {
        toolbarClose.setEnabled(enable);
    }

    public void setEnableRightButton(boolean enable) {
        toolbarBackup.setEnabled(enable);
    }

    public interface DefaultToolbarItemClick {
        void toolbarCloseClick();
    }

    public interface SyncQueueToolbarItemClick {
        void toolbarCloseClick();

        void toolbarSyncAllClick();
    }

    public interface EnrollProgramStageToolbarItemClick extends DefaultToolbarItemClick {
        void toolbarBackupClick();
    }

    public interface TemplateToolbarItemClick extends DefaultToolbarItemClick {
    }

}
