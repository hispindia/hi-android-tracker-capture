


package org.hisp.india.trackercapture.services.program_rule_variables;

import org.hisp.india.trackercapture.models.response.ProgramRuleVariablesResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ProgramRuleVariableService {

    Observable<ProgramRuleVariablesResponse> getProgramRuleVariables();

}
