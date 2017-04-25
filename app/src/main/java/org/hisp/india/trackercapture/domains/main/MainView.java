package org.hisp.india.trackercapture.domains.main;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.hisp.india.trackercapture.models.storage.TOrganizationUnit;
import org.hisp.india.trackercapture.models.storage.TProgram;

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

    /**
     * Display a loading view while loading data in background.
     */
    void hideLoading();

    void showOrgList(List<TOrganizationUnit> organizationUnitList);

    void showProgramList(List<TProgram> programList);
}
