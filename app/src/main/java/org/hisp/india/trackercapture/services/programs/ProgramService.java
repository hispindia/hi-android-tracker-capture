package org.hisp.india.trackercapture.services.programs;

import org.hisp.india.trackercapture.models.response.ProgramsResponse;

import rx.Observable;

/**
 * Created by nhancao on 4/24/17.
 */

public interface ProgramService {

    Observable<ProgramsResponse> getPrograms();

}
