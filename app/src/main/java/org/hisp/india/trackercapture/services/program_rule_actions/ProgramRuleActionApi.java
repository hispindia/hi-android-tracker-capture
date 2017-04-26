package org.hisp.india.trackercapture.services.program_rule_actions;

import org.hisp.india.trackercapture.models.response.ProgramRuleActionsResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ProgramRuleActionApi {

    @GET("api/programRuleActions?paging=false")
    Observable<ProgramRuleActionsResponse> getProgramRuleActions();

}
