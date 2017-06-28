package org.hisp.india.trackercapture.services.program_rule_actions;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ProgramRuleActionsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultProgramRuleActionService implements ProgramRuleActionService {

    private NetworkProvider networkProvider;
    private ProgramRuleActionApi restService;

    public DefaultProgramRuleActionService(NetworkProvider networkProvider,
                                           ProgramRuleActionApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(ProgramRuleActionApi programRuleActionApi) {
        restService = programRuleActionApi;
    }

    @Override
    public Observable<ProgramRuleActionsResponse> getProgramRuleActions() {
        return networkProvider
                .transformResponse(restService.getProgramRuleActions());
    }
}
