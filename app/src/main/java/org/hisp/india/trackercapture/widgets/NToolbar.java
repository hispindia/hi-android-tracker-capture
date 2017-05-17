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
    @ViewById(R.id.view_toolbar_setting)
    protected View toolbarSetting;

    public NToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void applyCommonUi(AppCompatActivity activity) {
        //Setup toolbar
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void applyTemplatelUi(AppCompatActivity activity, String title, TemplateToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarIcon.setVisibility(View.GONE);
        toolbarClose.setVisibility(View.VISIBLE);
        toolbarSetting.setVisibility(View.GONE);

        toolbarClose.setOnClickListener(v -> {
            itemClick.toolbarCloseClick();
        });

    }

    public void applyEnrollUi(AppCompatActivity activity, String title, EnrollToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarIcon.setVisibility(View.GONE);
        toolbarClose.setVisibility(View.VISIBLE);
        toolbarSetting.setVisibility(View.GONE);

        toolbarClose.setOnClickListener(v -> {
            itemClick.toolbarCloseClick();
        });

    }

    public void applyProgramStagelUi(AppCompatActivity activity, String title, ProgramStageToolbarItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarIcon.setVisibility(View.GONE);
        toolbarClose.setVisibility(View.VISIBLE);
        toolbarSetting.setVisibility(View.GONE);

        toolbarClose.setOnClickListener(v -> {
            itemClick.toolbarCloseClick();
        });

    }

    public interface TemplateToolbarItemClick {
        void toolbarCloseClick();
    }

    public interface EnrollToolbarItemClick {
        void toolbarCloseClick();
    }

    public interface ProgramStageToolbarItemClick {
        void toolbarCloseClick();
    }

}
