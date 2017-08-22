package org.hisp.india.trackercapture.services.enrollments;

import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface EnrollmentService {

    void setRestService(EnrollmentApi enrollmentApi);

    Observable<BaseResponse> postEnrollments(RTaskEnrollment enrollmentRequest);

    Observable<BaseResponse> putEnrollments(RTaskEnrollment enrollmentRequest);

}
