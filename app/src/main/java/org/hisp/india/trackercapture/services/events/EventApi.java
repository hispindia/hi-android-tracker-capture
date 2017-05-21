package org.hisp.india.trackercapture.services.events;

import org.hisp.india.trackercapture.models.request.EventRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface EventApi {

    @POST("api/events?paging=false")
    Observable<BaseResponse> getEvents(@Body EventRequest eventRequest);

}
