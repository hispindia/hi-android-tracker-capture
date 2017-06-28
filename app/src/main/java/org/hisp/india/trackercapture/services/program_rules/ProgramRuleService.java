package org.hisp.india.trackercapture.services.program_rules;

import org.hisp.india.trackercapture.models.response.ProgramRulesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ProgramRuleService {

    void setRestService(ProgramRuleApi programRuleApi);

    Observable<ProgramRulesResponse> getProgramRules();

}
