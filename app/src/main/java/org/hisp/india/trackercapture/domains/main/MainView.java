package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.hisp.india.trackercapture.models.response.EventsResponse;
import org.hisp.india.trackercapture.models.response.PageResponse;
import org.hisp.india.trackercapture.models.response.QueryResponse;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RTrackedEntityInstance;

import java.util.HashMap;
import java.util.List;

import ru.terrakok.cicerone.Navigator;

/**
 * Created by nhancao on 5/5/17.
 */

public interface MainView extends MvpView {
    Navigator getNavigator();

    /**
     * Display a loading view while loading data in background.
     */
    void showLoading();

    void showLoading(String msg);

    /**
     * Display a loading view while loading data in background.
     */
    void hideLoading();

    void showOrgList(List<ROrganizationUnit> organizationUnitList);

    void showProgramList(List<RProgram> programList);

    void queryProgramSuccess(QueryResponse queryResponse);

    void queryProgramSuccess(QueryResponse queryResponse,int page);//added by ifhaam

    void syncSuccessful();

    void showError(String error);

    void updateProgressStatus(String message);

    void hideCircleProgressView();

    //added by ifhaam on 28/09/2017
    void getEventsSuccess(EventsResponse eventResponse);
    void registerProgramSyncRequest(String msg);
    void downloadInstancesSuccess(QueryResponse queryResponse,HashMap<String,String> uuids);
    void downloadInstancesSuccess(QueryResponse queryResponse, HashMap<String,String> uuidList, List<RTrackedEntityInstance> trackedEntityInstances);
    void downloadInstancesSuccess(HashMap<String,String> uuidList, List<RTrackedEntityInstance> trackedEntityInstances, PageResponse pageResponse);
}
