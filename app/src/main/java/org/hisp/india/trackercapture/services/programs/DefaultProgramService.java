package org.hisp.india.trackercapture.services.programs;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.base.Program;
import org.hisp.india.trackercapture.models.response.ProgramsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultProgramService implements ProgramService {

    private NetworkProvider networkProvider;
    private ProgramApi restService;

    public DefaultProgramService(NetworkProvider networkProvider,
                                 ProgramApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(ProgramApi programApi) {
        restService = programApi;
    }

    @Override
    public Observable<ProgramsResponse> getPrograms() {
        return networkProvider
                .transformResponse(restService.getPrograms());
    }

    @Override
    public Observable<Program> getProgramDetail(String programId) {
        return networkProvider.transformResponse(restService.getProgramDetail(programId));
    }
}
