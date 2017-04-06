package org.hisp.india.trackercapture.domains.login;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.hisp.india.trackercapture.utils.PrefManager;

import java.util.List;

import javax.inject.Inject;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView, Validator.ValidationListener {

    @ViewById(R.id.activity_login_v_username_underline)
    View vUsernameUnderline;
    @ViewById(R.id.activity_login_v_password_underline)
    View vPasswordUnderline;
    @ViewById(R.id.activity_login_bt_login)
    Button btLogin;

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

    @Click(R.id.activity_login_bt_login)
    void btLoginClick() {
        if (isValid) {
            Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();

            PrefManager.setIsLogin(true);
            MainActivity_.intent(this).start();
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

}
