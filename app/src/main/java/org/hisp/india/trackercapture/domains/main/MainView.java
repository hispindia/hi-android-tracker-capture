package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.hisp.india.trackercapture.models.response.QueryResponse;
import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;

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

    void syncSuccessful();

    void showError(String error);

    void updateProgressStatus(String message);

}
