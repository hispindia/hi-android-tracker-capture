package org.hisp.india.trackercapture.services.events;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.request.EventRequest;
import org.hisp.india.trackercapture.models.response.BaseResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public class DefaultEventService implements EventService {

    private NetworkProvider networkProvider;
    private EventApi restService;

    public DefaultEventService(NetworkProvider networkProvider,
                               EventApi restService) {
        this.networkProvider = networkProvider;
        this.restService = restService;
    }

    @Override
    public void setRestService(EventApi eventApi) {

    }

    @Override
    public Observable<BaseResponse> postEvents(EventRequest eventRequest) {
        return networkProvider
                .transformResponse(restService.postEvents(eventRequest));
    }

    @Override
    public Observable<BaseResponse> putEvents(EventRequest eventRequest, String trackedEntityInstanceId) {
        return networkProvider
                .transformResponse(restService.putEvents(eventRequest, trackedEntityInstanceId));
    }
}
