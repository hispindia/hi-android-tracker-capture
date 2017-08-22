package org.hisp.india.trackercapture.services.events;

import org.hisp.india.trackercapture.models.request.EventRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface EventApi {

    @POST("api/events?paging=false")
    Observable<BaseResponse> postEvents(
            @Body
                    EventRequest eventRequest);

    @PUT("api/events/{trackedEntityInstanceId}?paging=false")
    Observable<BaseResponse> putEvents(
            @Body
                    EventRequest eventRequest,
            @Path("trackedEntityInstanceId")
                    String trackedEntityInstanceId);

}
