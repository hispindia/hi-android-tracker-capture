package org.hisp.india.trackercapture.services.account;

import com.orhanobut.hawk.Hawk;

import org.hisp.india.core.services.network.RxNetworkProvider;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.models.User;
import org.hisp.india.trackercapture.utils.Constants;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by nhancao on 4/9/17.
 */

public class DefaultAccountService implements AccountService {

    private RxNetworkProvider networkProvider;
    private AccountApi restService;
    private Credentials credentials;

    public DefaultAccountService(RxNetworkProvider networkProvider, AccountApi restService, Credentials credentials) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.credentials = credentials;
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
                .observeOn(Schedulers.computation())
                .flatMap(user -> {
                    credentials.setUserInfo(user);
                    Hawk.put(Constants.CREDENTIALS, credentials);
                    return Observable.just(user);
                });
    }

    @Override
    public void logout() {
        credentials.clear();
        Hawk.put(Constants.CREDENTIALS, credentials);
    }

}
