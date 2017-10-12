package org.hisp.india.trackercapture.services.programs;

import org.hisp.india.trackercapture.models.base.Program;
import org.hisp.india.trackercapture.models.response.ProgramsResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ProgramApi {

    @GET("api/programs?paging=false")
    Observable<ProgramsResponse> getPrograms();

    @GET("api/programs/{programId}?fields=id,displayName,trackedEntity,withoutRegistration,programRuleVariables[*]," +
                 "programStages[*,programStageSections[*,programStageDataElements[*,dataElement[*]]],programStageDataElements[*,dataElement[*,optionSet[id,displayName,valueType," +
                 "options[id,displayName]]]]],programRules[*,programRuleActions[id,programRuleActionType]]," +
                 "enrollmentDateLabel,selectEnrollmentDatesInFuture,incidentDateLabel,selectIncidentDatesInFuture," +
                 "displayIncidentDate,programTrackedEntityAttributes[*,trackedEntityAttribute[id,displayName,optionSetValue," +
                 "optionSet[id,displayName,valueType,options[id,displayName]]]]")
    Observable<Program> getProgramDetail(
            @Path("programId")
                    String programId);


}
