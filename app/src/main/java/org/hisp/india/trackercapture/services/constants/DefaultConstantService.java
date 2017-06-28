package org.hisp.india.trackercapture.services.constants;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ConstantsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultConstantService implements ConstantService {

    private NetworkProvider networkProvider;
    private ConstantApi restService;

    public DefaultConstantService(NetworkProvider networkProvider,
                                  ConstantApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(ConstantApi constantApi) {
        restService = constantApi;
    }

    @Override
    public Observable<ConstantsResponse> getConstants() {
        return networkProvider
                .transformResponse(restService.getConstants());
    }
}
