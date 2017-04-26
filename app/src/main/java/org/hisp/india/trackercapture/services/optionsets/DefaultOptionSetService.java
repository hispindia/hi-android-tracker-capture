package org.hisp.india.trackercapture.services.optionsets;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.OptionSetsResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultOptionSetService implements OptionSetService {

    private NetworkProvider networkProvider;
    private OptionSetApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultOptionSetService(NetworkProvider networkProvider,
                                   OptionSetApi restService,
                                   ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<OptionSetsResponse> getOptionSets() {
        return networkProvider
                .transformResponse(restService.getOptionSets())
                .compose(apiErrorFilter.execute());
    }
}
