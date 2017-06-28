package org.hisp.india.trackercapture.services.program_rules;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ProgramRulesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultProgramRuleService implements ProgramRuleService {

    private NetworkProvider networkProvider;
    private ProgramRuleApi restService;

    public DefaultProgramRuleService(NetworkProvider networkProvider,
                                     ProgramRuleApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(ProgramRuleApi programRuleApi) {
        restService = programRuleApi;
    }

    @Override
    public Observable<ProgramRulesResponse> getProgramRules() {
        return networkProvider
                .transformResponse(restService.getProgramRules());
    }
}
