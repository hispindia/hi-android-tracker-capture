package org.hisp.india.trackercapture.services.enrollments;

import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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


}
