package org.hisp.india.trackercapture.services.enrollments;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultEnrollmentService implements EnrollmentService {

    private NetworkProvider networkProvider;
    private EnrollmentApi restService;

    public DefaultEnrollmentService(NetworkProvider networkProvider,
                                    EnrollmentApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public Observable<BaseResponse> postEnrollments(EnrollmentRequest enrollmentRequest) {
        return networkProvider
                .transformResponse(restService.postEnrollments(enrollmentRequest));
    }
}
