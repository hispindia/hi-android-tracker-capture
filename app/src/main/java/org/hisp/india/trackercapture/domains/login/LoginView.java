package org.hisp.india.trackercapture.domains.login;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.hisp.india.trackercapture.models.User;

/**
 * Created by nhancao on 5/5/17.
 */

public interface LoginView extends MvpView {
    /**
     * Display a loading view while loading data in background.
     */
    void showLoading();

    /**
     * Display a loading view while loading data in background.
     */
    void hideLoading();

    /**
     * Login api successful.
     */
    void loginSuccessful(User user);

    /**
     * Api error.
     *
     * @param throwable
     */
    void loginError(Throwable throwable);
}
