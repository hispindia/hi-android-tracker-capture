package org.hisp.india.trackercapture.services.program_rule_variables;

import org.hisp.india.trackercapture.models.response.ProgramRuleVariablesResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ProgramRuleVariableApi {

    @GET("api/programRuleVariables?paging=false")
    Observable<ProgramRuleVariablesResponse> getProgramRuleVariables();

}
