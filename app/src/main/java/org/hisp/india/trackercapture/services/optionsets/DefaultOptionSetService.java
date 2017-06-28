package org.hisp.india.trackercapture.services.optionsets;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.OptionSetsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultOptionSetService implements OptionSetService {

    private NetworkProvider networkProvider;
    private OptionSetApi restService;

    public DefaultOptionSetService(NetworkProvider networkProvider,
                                   OptionSetApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(OptionSetApi optionSetApi) {
        restService = optionSetApi;
    }

    @Override
    public Observable<OptionSetsResponse> getOptionSets() {
        return networkProvider
                .transformResponse(restService.getOptionSets());
    }
}
