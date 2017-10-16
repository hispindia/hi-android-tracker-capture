package org.hisp.india.trackercapture.services.enrollments;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.response.EnrollmentsResponse;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;

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
    public void setRestService(EnrollmentApi enrollmentApi) {
        restService = enrollmentApi;
    }

    @Override
    public Observable<BaseResponse> postEnrollments(RTaskEnrollment enrollmentRequest) {
        return networkProvider
                .transformResponse(restService.postEnrollments(enrollmentRequest));
    }

    @Override
    public Observable<BaseResponse> putEnrollments(RTaskEnrollment enrollmentRequest) {
        return networkProvider
                .transformResponse(restService.putEnrollments(enrollmentRequest,
                                                              enrollmentRequest.getEnrollmentId()));
    }

    //added by ifhaam on 10/03/2017
    @Override
    public Observable<EnrollmentsResponse> getEnrollments(String trackedEntityInstance){
        return networkProvider.transformResponse(restService.getEnrollments(trackedEntityInstance));
    }

    @Override
    public Observable<EnrollmentsResponse> getEnrollments(String orgUnit,String trackedEntityInstance,String program){
        return networkProvider.transformResponse(restService.getEnrollments(orgUnit,trackedEntityInstance,program));
    }
}
