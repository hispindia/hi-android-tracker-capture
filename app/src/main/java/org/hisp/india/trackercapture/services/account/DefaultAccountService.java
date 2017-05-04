package org.hisp.india.trackercapture.services.account;

import android.util.Log;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.models.base.User;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;
import org.hisp.india.trackercapture.services.filter.AuthenticationSuccessFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/9/17.
 */

public class DefaultAccountService implements AccountService {
    private static final String TAG = DefaultAccountService.class.getSimpleName();

    private NetworkProvider networkProvider;
    private AccountApi restService;
    private Credentials credentials;
    private ApiErrorFilter apiErrorFilter;

    public DefaultAccountService(NetworkProvider networkProvider, AccountApi restService, Credentials credentials,
                                 ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.credentials = credentials;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public boolean isLogin() {
        Log.e(TAG, "isLogin: " + credentials.isLoginSuccess());
        return credentials.isLoginSuccess();
    }

    @Override
    public void updateCredentialHost(String host) {
        credentials.setHost(host);

        restService = networkProvider
                .addDefaultHeader()
                .addHeader("Authorization", credentials.getApiToken())
                .provideApi(credentials.getHost(), AccountApi.class);

    }

    @Override
    public void updateCredentialToken(String username, String password) {
        credentials.setApiToken(username, password);
        restService = networkProvider
                .addDefaultHeader()
                .addHeader("Authorization", credentials.getApiToken())
                .provideApi(credentials.getHost(), AccountApi.class);

    }

    @Override
    public Observable<User> login() {
        return networkProvider
                .transformResponse(restService.login(
                        "id,created,lastUpdated,name,displayName,firstName,surname,gender," +
                        "birthday,introduction,education,employer,interests,jobTitle,languages," +
                        "email,phoneNumber,organisationUnits[id,displayName,programs[id,displayName]]"
                                                    ))
                .compose(new AuthenticationSuccessFilter(credentials).execute())
                .compose(apiErrorFilter.execute());
    }

    @Override
    public void logout() {
        credentials.clear();
    }

}
