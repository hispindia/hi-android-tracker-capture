package org.hisp.india.trackercapture.services.constants;

import org.hisp.india.trackercapture.models.response.ConstantsResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ConstantApi {

    @GET("api/constants?paging=false")
    Observable<ConstantsResponse> getConstants();

}
