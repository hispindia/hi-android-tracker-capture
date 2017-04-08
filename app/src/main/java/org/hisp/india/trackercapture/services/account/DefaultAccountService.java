package org.hisp.india.trackercapture.services.account;

import org.hisp.india.core.services.network.RxNetworkProvider;
import org.hisp.india.trackercapture.models.UserResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/9/17.
 */

public class DefaultAccountService implements AccountService {

    private RxNetworkProvider networkProvider;
    private AccountApi restService;

    public DefaultAccountService(RxNetworkProvider networkProvider, AccountApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void updateCredential(String host, String apiToken) {

        restService = networkProvider
                .addDefaultHeader()
                .addHeader("Authorization", apiToken)
                .provideApi(host, AccountApi.class);

    }

    @Override
    public Observable<UserResponse> login() {
        return networkProvider.transformResponse(restService.login(
                "id,created,lastUpdated,name,displayName,firstName,surname,gender,birthday,introduction,education,employer,interests,jobTitle,languages,email,phoneNumber,teiSearchOrganisationUnits[id],organisationUnits[id]"
        ));
    }

}
