package org.hisp.india.trackercapture.services.optionsets;

import org.hisp.india.trackercapture.models.response.OptionSetsResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface OptionSetApi {

    @GET("api/optionSets?paging=false")
    Observable<OptionSetsResponse> getOptionSets();

}
