package org.hisp.india.trackercapture.domains.login;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.domains.main.MainActivity_;
import org.hisp.india.trackercapture.models.base.User;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.utils.Constants;
import org.hisp.india.trackercapture.utils.NKeyboard;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Replace;
import rx.Observable;
import rx.Subscription;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity<LoginView, LoginPresenter>
        implements LoginView, Validator.ValidationListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @ViewById(R.id.activity_login_root)
    protected View vRoot;
    @ViewById(R.id.activity_login_ll_host)
    protected LinearLayout llHost;
    @ViewById(R.id.activity_login_et_host)
    protected EditText etHost;

    @ViewById(R.id.activity_login_v_username_underline)
    protected View vUsernameUnderline;
    @ViewById(R.id.activity_login_v_password_underline)
    protected View vPasswordUnderline;
    @ViewById(R.id.activity_login_bt_login)
    protected Button btLogin;
    @ViewById(R.id.activity_login_root_scroll)
    protected ScrollView rootScroll;
    @ViewById(R.id.activity_login_v_bottom_space)
    protected View vBottomSpace;

    @NotEmpty
    @Length(min = 4)
    @ViewById(R.id.activity_login_et_username)
    protected EditText etUsername;

    @NotEmpty
    @Password(min = 6)
    @ViewById(R.id.activity_login_et_password)
    protected EditText etPassword;

    @App
    protected MainApplication application;
    @Inject
    protected LoginPresenter presenter;

    private Validator validator;
    private boolean isValid;
    private int tapCounter;
    private Subscription subscription;

    private Navigator navigator = command -> {
        if (command instanceof Replace) {
            if (((Replace) command).getScreenKey().equals(Screens.MAIN_SCREEN)) {
                MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                             .start();
            }
        }
    };

    @AfterInject
    void inject() {
        DaggerLoginComponent.builder()
                            .applicationComponent(application.getApplicationComponent())
                            .build()
                            .inject(this);
    }

    @AfterViews
    void init() {

        validator = new Validator(this);
        validator.setValidationListener(this);

        AppUtils.setupHideKeyboardWhenTouchOutside(vRoot);
        NKeyboard.addListener(this, (isVisible, screenHeight, keyboardHeight) -> {
            if (isVisible && keyboardHeight > 0) {

                int offset = AppUtils.convertDpToPixels(228, this);
                View view = getCurrentFocus();
                if (view != null) {
                    if (view == etUsername) {
                        offset = AppUtils.convertDpToPixels(164, this);
                    } else if (view == etPassword) {
                        if (llHost.getVisibility() == View.VISIBLE) {
                            offset = AppUtils.convertDpToPixels(100, this);
                        } else {
                            offset = AppUtils.convertDpToPixels(100, this);
                        }
                    }
                }
                ObjectAnimator.ofInt(rootScroll, "scrollY", offset).setDuration(500).start();
                AppUtils.animationHeight(vBottomSpace, 0, keyboardHeight, 500);
            } else {
                ObjectAnimator.ofInt(rootScroll, "scrollY", 0).setDuration(500).start();
                AppUtils.animationHeight(vBottomSpace, vBottomSpace.getHeight(), 0, 500);
            }
        });

        //Auto fill host
        if (!presenter.getCredential().getHost().equals(Constants.HOST_DEFAULT)) {
            etHost.setText(presenter.getCredential().getHost());
        } else {
            llHost.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onValidationSucceeded() {
        isValid = true;
        btLogin.setEnabled(true);

        vUsernameUnderline.setBackgroundColor(Color.parseColor("#626262"));
        vPasswordUnderline.setBackgroundColor(Color.parseColor("#626262"));

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        isValid = false;
        btLogin.setEnabled(false);

        vUsernameUnderline.setBackgroundColor(Color.parseColor("#626262"));
        vPasswordUnderline.setBackgroundColor(Color.parseColor("#626262"));

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
                if (view.equals(etUsername)) {
                    vUsernameUnderline.setBackgroundColor(Color.RED);
                } else {
                    vPasswordUnderline.setBackgroundColor(Color.RED);
                }
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Click(R.id.activity_login_fl_icon)
    void flIconClick() {
        tapCounter++;
        if (tapCounter >= 7) {
            llHost.setVisibility(View.VISIBLE);
        } else {
            llHost.setVisibility(View.GONE);
        }

        RxScheduler.onStop(subscription);
        subscription = Observable.empty()
                                 .delay(2, TimeUnit.SECONDS)
                                 .doOnTerminate(() -> tapCounter = 0)
                                 .subscribe();

    }

    @Click(R.id.activity_login_bt_login)
    void btLoginClick() {
        if (isValid) {
            if (llHost.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(etHost.getText())) {
                String host = etHost.getText().toString();
                if (!host.contains("http")) {
                    host = "http://" + host;
                }
                if (host.charAt(host.length() - 1) != '/') {
                    host += "/";
                }
                presenter.updateCredentialHost(host);
            }
            presenter.updateCredentialTokenAndLogin(etUsername.getText().toString(), etPassword.getText().toString());

        }
    }

    @TextChange(R.id.activity_login_et_username)
    void onUsernameTextChanges(TextView tv, CharSequence text) {
        validator.validate();
    }

    @TextChange(R.id.activity_login_et_password)
    void onPasswordTextChanges(TextView tv, CharSequence text) {
        validator.validate();
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
    public void showLoading(String msg) {
        showProgressLoading(msg);
    }

    @Override
    public void hideLoading() {
        hideProgressLoading();
    }

    @Override
    public void loginSuccessful(User user) {
        Toast.makeText(application, "Login success", Toast.LENGTH_SHORT).show();
        application.refreshApplicationModule();
    }

    @Override
    public void loginError(Throwable throwable) {
        showErrorMessage(throwable.getMessage());
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
    }
}
