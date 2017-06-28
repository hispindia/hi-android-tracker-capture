package org.hisp.india.trackercapture.services.constants;

import org.hisp.india.trackercapture.models.response.ConstantsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ConstantService {

    void setRestService(ConstantApi constantApi);

    Observable<ConstantsResponse> getConstants();

}
