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
    Toolbar toolbar;
    @ViewById(R.id.view_toolbar_icon)
    View toolbarIcon;
    @ViewById(R.id.view_toolbar_close)
    View toolbarClose;
    @ViewById(R.id.view_toolbar_tv_title)
    TextView tvTitle;
    @ViewById(R.id.view_toolbar_setting)
    View toolbarSetting;

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
        toolbarSetting.setVisibility(View.VISIBLE);

        toolbarClose.setOnClickListener(v -> {
            itemClick.toolbarCloseClick();
        });
        toolbarSetting.setOnClickListener(v -> {
            itemClick.toolbarSettingClick();
        });

    }

    public interface TemplateToolbarItemClick {
        void toolbarCloseClick();
    }


    public interface EnrollToolbarItemClick {
        void toolbarCloseClick();

        void toolbarSettingClick();
    }

}
