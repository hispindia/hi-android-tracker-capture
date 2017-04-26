package org.hisp.india.trackercapture.services.program_rule_actions;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ProgramRuleActionsResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultProgramRuleActionService implements ProgramRuleActionService {

    private NetworkProvider networkProvider;
    private ProgramRuleActionApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultProgramRuleActionService(NetworkProvider networkProvider,
                                           ProgramRuleActionApi restService,
                                           ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<ProgramRuleActionsResponse> getProgramRuleActions() {
        return networkProvider
                .transformResponse(restService.getProgramRuleActions())
                .compose(apiErrorFilter.execute());
    }
}
