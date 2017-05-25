package org.hisp.india.trackercapture.domains.enroll_program_stage_detail;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.hisp.india.trackercapture.models.storage.RProgramStageDataElement;

import java.util.List;

import ru.terrakok.cicerone.Navigator;

/**
 * Created by nhancao on 5/5/17.
 */

public interface EnrollProgramStageDetailView extends MvpView {

    Navigator getNavigator();

    /**
     * Display a loading view while loading data in background.
     */
    void showLoading();

    /**
     * Display a loading view while loading data in background.
     */
    void hideLoading();

    void getProgramStageDetail(List<RProgramStageDataElement> programStageDataElements);

}
