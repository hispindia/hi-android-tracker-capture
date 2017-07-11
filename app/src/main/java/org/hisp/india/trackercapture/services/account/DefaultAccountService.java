package org.hisp.india.trackercapture.services.account;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.models.base.User;
import org.hisp.india.trackercapture.services.filter.AuthenticationSuccessFilter;
import org.hisp.india.trackercapture.utils.RealmHelper;

import rx.Observable;

/**
 * Created by nhancao on 4/9/17.
 */

public class DefaultAccountService implements AccountService {
    private static final String TAG = DefaultAccountService.class.getSimpleName();
    private NetworkProvider networkProvider;
    private AccountApi restService;
    private Credentials credentials;

    public DefaultAccountService(NetworkProvider networkProvider, AccountApi restService, Credentials credentials) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.credentials = credentials;
    }

    @Override
    public void setRestService(AccountApi accountApi) {
        restService = accountApi;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public boolean isLogin() {
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
                .transformResponse(restService.login())
                .compose(new AuthenticationSuccessFilter(credentials).execute());
    }

    @Override
    public void logout() {
        RealmHelper.transaction(realm -> realm.deleteAll());
        credentials.clear();
    }

}
