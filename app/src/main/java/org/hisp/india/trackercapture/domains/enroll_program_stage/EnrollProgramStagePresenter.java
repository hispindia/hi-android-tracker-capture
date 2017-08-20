package org.hisp.india.trackercapture.domains.enroll_program_stage;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.storage.RTaskTrackedEntityInstance;
import org.hisp.india.trackercapture.navigator.Screens;

import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 5/5/17.
 */

public class EnrollProgramStagePresenter extends MvpBasePresenter<EnrollProgramStageView> {
    private static final String TAG = EnrollProgramStagePresenter.class.getSimpleName();

    private NavigatorHolder navigatorHolder;
    private Router router;

    @Inject
    public EnrollProgramStagePresenter(Router router, NavigatorHolder navigatorHolder) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
    }

    @Override
    public void attachView(EnrollProgramStageView view) {
        super.attachView(view);
        navigatorHolder.setNavigator(view.getNavigator());
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        super.detachView(retainInstance);
    }

    public void onBackCommandClick() {
        router.exit();
    }

    public void registerProgram(RTaskTrackedEntityInstance trackedEntityInstance,
                                RTaskEnrollment enrollment, List<RTaskEvent> eventList) {
        RTaskRequest taskRequest = RTaskRequest.create(trackedEntityInstance, enrollment, eventList);
        taskRequest.save();
        getView().showToastMessage("Added into queue.");
        router.exit();
    }

    public void openProgramStage(RProgramStage programStage) {
        router.navigateTo(Screens.ENROLL_PROGRAM_STAGE_DETAIL, programStage);
    }

}
