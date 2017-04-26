package org.hisp.india.trackercapture.services.program_rule_variables;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ProgramRuleVariablesResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultProgramRuleVariableService implements ProgramRuleVariableService {

    private NetworkProvider networkProvider;
    private ProgramRuleVariableApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultProgramRuleVariableService(NetworkProvider networkProvider,
                                             ProgramRuleVariableApi restService,
                                             ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<ProgramRuleVariablesResponse> getProgramRuleVariables() {
        return networkProvider
                .transformResponse(restService.getProgramRuleVariables())
                .compose(apiErrorFilter.execute());
    }
}
