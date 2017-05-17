package org.hisp.india.trackercapture.domains.enroll_program_stage;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.hisp.india.trackercapture.models.response.BaseResponse;
import org.hisp.india.trackercapture.models.storage.RProgram;

import ru.terrakok.cicerone.Navigator;

/**
 * Created by nhancao on 5/5/17.
 */

public interface EnrollProgramStageView extends MvpView {

    Navigator getNavigator();

    /**
     * Display a loading view while loading data in background.
     */
    void showLoading();

    /**
     * Display a loading view while loading data in background.
     */
    void hideLoading();

    void getProgramDetail(RProgram programDetail);

    void registerProgramSuccess(BaseResponse baseResponse);
}
