package org.hisp.india.trackercapture.services.tracked_entity_instances;

import org.hisp.india.trackercapture.models.base.TrackedEntityInstance;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.response.QueryResponse;
import org.hisp.india.trackercapture.models.response.TrackedEntityInstancesResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface TrackedEntityInstanceApi {

    @POST("api/trackedEntityInstances")
    Observable<BaseResponse> postTrackedEntityInstances(
            @Body TrackedEntityInstanceRequest trackedEntityInstanceRequest);

    @GET("api/trackedEntityInstances/query.json?&ouMode=DESCENDANTS&paging=false")
    Observable<QueryResponse> queryTrackedEntityInstances(@Query("ou") String orgUnitId,
                                                          @Query("program") String programId,
                                                          @Query("programStatus") String programStatus);

    @GET("api/trackedEntityInstances?paging=false&fields=*")
    Observable<TrackedEntityInstancesResponse> getTrackedEntityInstances(@Query("ou") String orgUnitId,
                                                                         @Query("program") String programId);


}
