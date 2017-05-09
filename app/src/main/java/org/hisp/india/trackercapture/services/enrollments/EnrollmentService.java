package org.hisp.india.trackercapture.services.enrollments;

import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface EnrollmentService {

    Observable<BaseResponse> postEnrollments(EnrollmentRequest enrollmentRequest);

}
