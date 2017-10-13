package org.hisp.india.trackercapture.services.events;

import org.hisp.india.trackercapture.models.base.Event;
import org.hisp.india.trackercapture.models.request.EventRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface EventService {

    void setRestService(EventApi eventApi);

    Observable<BaseResponse> postEvents(EventRequest eventRequest);

    Observable<BaseResponse> putEvents(EventRequest eventRequest, String trackedEntityInstanceId);

    Observable<BaseResponse> putEvent(RTaskEvent event, String eventId);

}
