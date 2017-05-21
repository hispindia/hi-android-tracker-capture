package org.hisp.india.trackercapture.services.enrollments;

import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface EnrollmentApi {

    @POST("api/enrollments")
    Observable<BaseResponse> postEnrollments(@Body EnrollmentRequest enrollmentRequest);


}
