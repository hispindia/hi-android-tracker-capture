package org.hisp.india.trackercapture.services.constants;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ConstantsResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultConstantService implements ConstantService {

    private NetworkProvider networkProvider;
    private ConstantApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultConstantService(NetworkProvider networkProvider,
                                  ConstantApi restService,
                                  ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<ConstantsResponse> getConstants() {
        return networkProvider
                .transformResponse(restService.getConstants())
                .compose(apiErrorFilter.execute());
    }
}
