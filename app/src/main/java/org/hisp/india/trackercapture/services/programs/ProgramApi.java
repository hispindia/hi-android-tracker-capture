package org.hisp.india.trackercapture.services.programs;

import org.hisp.india.trackercapture.models.response.ProgramDetailResponse;
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

    @GET("api/programs/{programId}?fields=id,displayName,programRuleVariables[*],programStages[*],programRules[*]," +
         "enrollmentDateLabel,selectEnrollmentDatesInFuture,incidentDateLabel,selectIncidentDatesInFuture," +
         "programTrackedEntityAttributes[*]")
    Observable<ProgramDetailResponse> getProgramDetail(
            @Path("programId")
                    String programId);


}
