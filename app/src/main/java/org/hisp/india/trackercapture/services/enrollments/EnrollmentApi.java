package org.hisp.india.trackercapture.services.enrollments;

import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.response.EnrollmentsResponse;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface EnrollmentApi {

    @POST("api/enrollments")
    Observable<BaseResponse> postEnrollments(
            @Body
                    RTaskEnrollment enrollmentRequest);

    @PUT("api/enrollments/{enrollmentId}")
    Observable<BaseResponse> putEnrollments(
            @Body
                    RTaskEnrollment enrollmentRequest,
            @Path("enrollmentId")
                    String enrollmentId);


    //added by ifhaam 03/10/2017
    @GET("api/enrollments.json?")
    Observable<EnrollmentsResponse> getEnrollments(
            @Query("trackedEntityInstance")String trackedEntityInstance
    );

    @GET("api/enrollments.json?")
    Observable<EnrollmentsResponse> getEnrollments(
            @Query("ou")String orgUnit,
            @Query("trackedEntityInstance")String trackedEntityInstance,
            @Query("program")String program
    );

}
