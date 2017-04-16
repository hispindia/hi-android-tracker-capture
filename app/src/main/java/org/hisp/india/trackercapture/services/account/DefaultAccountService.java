package org.hisp.india.trackercapture.services.account;

import com.orhanobut.hawk.Hawk;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.models.User;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;
import org.hisp.india.trackercapture.services.filter.AuthenticationSuccessFilter;
import org.hisp.india.trackercapture.utils.Constants;

import rx.Observable;

/**
 * Created by nhancao on 4/9/17.
 */

public class DefaultAccountService implements AccountService {

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
        return credentials.getUserInfo() != null;
    }

    @Override
    public void updateCredentialHost(String host) {
        credentials.setHost(host);

        restService = networkProvider
                .addDefaultHeader()
                .addHeader("Authorization", credentials.getApiToken())
                .provideApi(credentials.getHost(), AccountApi.class);

        Hawk.put(Constants.CREDENTIALS, credentials);

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
                        "id,created,lastUpdated,name,displayName,firstName,surname,gender,birthday,introduction,education,employer,interests,jobTitle,languages,email,phoneNumber,teiSearchOrganisationUnits[id],organisationUnits[id]"
                                                    ))
                .compose(new AuthenticationSuccessFilter(credentials).execute())
                .compose(apiErrorFilter.execute());
    }

    @Override
    public void logout() {
        credentials.clear();
        Hawk.put(Constants.CREDENTIALS, credentials);
    }

}
