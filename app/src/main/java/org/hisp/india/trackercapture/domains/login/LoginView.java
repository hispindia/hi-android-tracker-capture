package org.hisp.india.trackercapture.domains.login;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.hisp.india.trackercapture.models.base.User;

import ru.terrakok.cicerone.Navigator;

/**
 * Created by nhancao on 5/5/17.
 */

public interface LoginView extends MvpView {
    Navigator getNavigator();

    /**
     * Display a loading view while loading data in background.
     */
    void showLoading();

    void showLoading(String msg);

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
     */
    void showErrorMessage(String message);

    void updateProgressStatus(String message);

    void hideCircleProgressView();

}
