package org.hisp.india.trackercapture.services.account;

import org.hisp.india.core.services.RestMessageResponse;
import org.hisp.india.trackercapture.models.UserResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nhancao on 4/9/17.
 */

public interface AccountApi {

    @GET("api/me")
    Observable<RestMessageResponse<UserResponse>> login(@Query("fields") String fields);
}
