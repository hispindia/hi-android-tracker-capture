package org.hisp.india.trackercapture.services.program_rules;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.ProgramRulesResponse;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultProgramRuleService implements ProgramRuleService {

    private NetworkProvider networkProvider;
    private ProgramRuleApi restService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultProgramRuleService(NetworkProvider networkProvider,
                                     ProgramRuleApi restService,
                                     ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restService = restService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<ProgramRulesResponse> getProgramRules() {
        return networkProvider
                .transformResponse(restService.getProgramRules())
                .compose(apiErrorFilter.execute());
    }
}
