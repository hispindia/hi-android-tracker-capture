


package org.hisp.india.trackercapture.services.optionsets;

import org.hisp.india.trackercapture.models.response.OptionSetsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface OptionSetService {

    Observable<OptionSetsResponse> getOptionSets();

}
