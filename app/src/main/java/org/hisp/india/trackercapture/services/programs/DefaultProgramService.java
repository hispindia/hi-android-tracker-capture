package org.hisp.india.trackercapture.services.programs;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ProgramsResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultProgramService implements ProgramService {

    private NetworkProvider networkProvider;
    private ProgramApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultProgramService(NetworkProvider networkProvider,
                                 ProgramApi restService,
                                 ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<ProgramsResponse> getPrograms() {
        return networkProvider
                .transformResponse(restService.getPrograms())
                .compose(apiErrorFilter.execute());
    }
}
