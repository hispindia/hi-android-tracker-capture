package org.hisp.india.trackercapture.services.program_rule_variables;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ProgramRuleVariablesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultProgramRuleVariableService implements ProgramRuleVariableService {

    private NetworkProvider networkProvider;
    private ProgramRuleVariableApi restService;

    public DefaultProgramRuleVariableService(NetworkProvider networkProvider,
                                             ProgramRuleVariableApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(ProgramRuleVariableApi programRuleVariableApi) {
        restService = programRuleVariableApi;
    }

    @Override
    public Observable<ProgramRuleVariablesResponse> getProgramRuleVariables() {
        return networkProvider
                .transformResponse(restService.getProgramRuleVariables());
    }
}
