package org.hisp.india.trackercapture.domains.login;

import android.animation.ObjectAnimator;
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
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.base.BaseActivity;
import org.hisp.india.trackercapture.domains.main.MainActivity_;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.models.User;
import org.hisp.india.trackercapture.utils.AppUtils;
import org.hisp.india.trackercapture.utils.NKeyboard;
import org.hisp.india.trackercapture.utils.PrefManager;
import org.hisp.india.trackercapture.utils.RxHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView, Validator.ValidationListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @ViewById(R.id.activity_login_ll_host)
    LinearLayout llHost;
    @ViewById(R.id.activity_login_et_host)
    EditText etHost;

    @ViewById(R.id.activity_login_v_username_underline)
    View vUsernameUnderline;
    @ViewById(R.id.activity_login_v_password_underline)
    View vPasswordUnderline;
    @ViewById(R.id.activity_login_bt_login)
    Button btLogin;
    @ViewById(R.id.activity_login_root_scroll)
    ScrollView rootScroll;
    @ViewById(R.id.activity_login_v_bottom_space)
    View vBottomSpace;

    @NotEmpty
    @Length(min = 4)
    @ViewById(R.id.activity_login_et_username)
    EditText etUsername;

    @NotEmpty
    @Password(min = 6)
    @ViewById(R.id.activity_login_et_password)
    EditText etPassword;

    @App
    MainApplication application;
    @Inject
    LoginPresenter presenter;

    private Validator validator;
    private boolean isValid;
    private int tapCounter;
    private Subscription subscription;

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

        NKeyboard.addListener(this, (isVisible, screenHeight, keyboardHeight) -> {
            if (isVisible && keyboardHeight > 0) {
                ObjectAnimator.ofInt(rootScroll, "scrollY", keyboardHeight).setDuration(500).start();
                AppUtils.animationHeight(vBottomSpace, 0, keyboardHeight, 500);
            } else {
                ObjectAnimator.ofInt(rootScroll, "scrollY", 0).setDuration(500).start();
                AppUtils.animationHeight(vBottomSpace, vBottomSpace.getHeight(), 0, 500);
            }
        });

        //Auto fill host
        if (!PrefManager.getHost().equals(PrefManager.HOST_DEFAULT)) {
            etHost.setText(PrefManager.getHost());
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

        RxHelper.onStop(subscription);
        subscription = Observable.empty()
                .delay(2, TimeUnit.SECONDS)
                .doOnTerminate(() -> tapCounter = 0)
                .subscribe();

    }

    @Click(R.id.activity_login_bt_login)
    void btLoginClick() {
        if (isValid) {
            if (llHost.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(etHost.getText())) {
                PrefManager.setHost(etHost.getText().toString());
            }
            Credentials credentials = new Credentials(etUsername.getText().toString(), etPassword.getText().toString());
            PrefManager.setApiToken(credentials.genBasicToken());

            presenter.updateCredentialAndLogin(PrefManager.getHost(), PrefManager.getApiToken());

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
    public void showLoading() {
        showProgressLoading();
    }

    @Override
    public void hideLoading() {
        hideProgressLoading();
    }

    @Override
    public void loginSuccessful(User user) {
        Toast.makeText(application, "Login success", Toast.LENGTH_SHORT).show();
        PrefManager.setUserInfo(user);
        MainActivity_.intent(this).start();
    }

    @Override
    public void loginError(Throwable throwable) {
        Toast.makeText(application, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        PrefManager.setApiToken(null);

    }
}
