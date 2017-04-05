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

    //Update for main ui
    public void applyMainUi(AppCompatActivity activity, String title, MainItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarIcon.setVisibility(View.VISIBLE);
        toolbarClose.setVisibility(View.GONE);
        toolbarSetting.setVisibility(View.VISIBLE);

        toolbarIcon.setOnClickListener(v -> {
            itemClick.toolbarIconClick();
        });
        toolbarSetting.setOnClickListener(v -> {
            itemClick.toolbarProfileClick();
        });

    }


    public interface MainItemClick {
        void toolbarIconClick();

        void toolbarProfileClick();
    }

}
