package org.hisp.india.trackercapture.domains.main;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.nhancv.npermission.NPermission;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.domains.enroll.EnrollActivity_;
import org.hisp.india.trackercapture.domains.menu.DrawerAdapter;
import org.hisp.india.trackercapture.domains.menu.DrawerItem;
import org.hisp.india.trackercapture.domains.menu.MenuItem;
import org.hisp.india.trackercapture.domains.menu.SimpleItem;
import org.hisp.india.trackercapture.domains.menu.SpaceItem;
import org.hisp.india.trackercapture.models.User;
import org.hisp.india.trackercapture.utils.AppUtils;

import java.util.Arrays;

import javax.inject.Inject;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainView, MainPresenter>
        implements MainView, DrawerAdapter.OnItemSelectedListener,
                   NPermission.OnPermissionResult {
    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.activity_main_toolbar)
    Toolbar toolbar;

    @App
    MainApplication application;
    @Inject
    MainPresenter presenter;

    private NPermission nPermission;

    @AfterInject
    void inject() {
        DaggerMainComponent.builder()
                           .applicationComponent(application.getApplicationComponent())
                           .build()
                           .inject(this);
    }

    @AfterViews
    void init() {
        //@nhancv TODO: force request sdcard permission
        nPermission = new NPermission(true);

        //Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Setup toolbar
        setSupportActionBar(toolbar);

        //Built menu
        SlidingRootNavLayout navLayout = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.menu_drawer)
                .inject()
                .getLayout();
        TextView tvCharacterName = (TextView) navLayout.findViewById(R.id.menu_drawer_tv_character_name);
        TextView tvDisplayName = (TextView) navLayout.findViewById(R.id.menu_drawer_tv_display_name);
        TextView tvEmail = (TextView) navLayout.findViewById(R.id.menu_drawer_tv_email);

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(MenuItem.ENROLL).setChecked(true),
                createItemFor(MenuItem.SETTINGS),
                createItemFor(MenuItem.INFO),
                new SpaceItem(48),
                createItemFor(MenuItem.LOGOUT)));

        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.menu_drawer_rv_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        //Update info
        User user = presenter.getUserInfo();
        String character = "";
        if (user != null) {
            if (!TextUtils.isEmpty(user.getFirstName()) && !TextUtils.isEmpty(user.getSurName())) {
                character = String.valueOf(user.getFirstName().charAt(0)) +
                            String.valueOf(user.getSurName().charAt(0));
            } else if (!TextUtils.isEmpty(user.getDisplayName()) && user.getDisplayName().length() > 1) {
                character = String.valueOf(user.getDisplayName().charAt(0)) +
                            String.valueOf(user.getDisplayName().charAt(1));
            }
            tvCharacterName.setText(character);
            tvDisplayName.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        nPermission.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull
                                                   String[] permissions,
                                           @NonNull
                                                   int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        nPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(String permission, boolean isGranted) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (isGranted) {
                    nPermission.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    nPermission.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                if (isGranted) {
                    //@nhancv TODO: after get all required permission
                    application.initRealmConfig();
                    presenter.getOrganizations();
                } else {
                    nPermission.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(MenuItem menuItem) {
        switch (menuItem) {
            case ENROLL:
                EnrollActivity_.intent(this).start();
                break;
            case LOGOUT:
                presenter.logout();
                recreate();
                break;
        }
    }

    @Override
    public void showLoading() {
        showProgressLoading();
    }

    @Override
    public void hideLoading() {
        hideProgressLoading();
    }

    private DrawerItem createItemFor(MenuItem menuItem) {
        return new SimpleItem(menuItem.getTitle(), ContextCompat.getDrawable(this, menuItem.getIcon()));
    }


}
