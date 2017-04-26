package org.hisp.india.trackercapture.services.program_rules;

import org.hisp.india.trackercapture.models.response.ProgramRulesResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ProgramRuleApi {

    @GET("api/programRules?paging=false")
    Observable<ProgramRulesResponse> getProgramRules();

}
