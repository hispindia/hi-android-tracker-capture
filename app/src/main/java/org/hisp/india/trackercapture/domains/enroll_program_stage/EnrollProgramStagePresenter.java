package org.hisp.india.trackercapture.domains.enroll_program_stage;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.tmp.TMEnrollProgram;
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

    public void registerProgram(TMEnrollProgram tmEnrollProgram, List<RTaskEvent> eventList) {
        RTaskRequest taskRequest = tmEnrollProgram.getTaskRequest();
        if (taskRequest == null) {
            taskRequest = RTaskRequest.create(tmEnrollProgram.getTaskRequest().getTrackedEntityInstance(),
                                              tmEnrollProgram.getTaskRequest().getEnrollment(),
                                              eventList);
        } else {
            taskRequest.setNeedSync(true);
            for(RTaskEvent taskEvent :taskRequest.getEventList()){
                for(RTaskEvent rTaskEvent:eventList){
                    if(taskEvent.getProgramStageId().equals(rTaskEvent.getProgramStageId())){
                        rTaskEvent.setEvent(taskEvent.getEvent());
                        break;
                    }
                }
            }
            taskRequest.setEventList(eventList);
        }
        taskRequest.save();
        getView().registerProgramSyncRequest("Added into queue.");
        router.exit();
    }

    public void openProgramStage(RProgramStage programStage) {
        router.navigateTo(Screens.ENROLL_PROGRAM_STAGE_DETAIL, programStage);
    }

    public void enrollAnotherProgram(TMEnrollProgram tmEnrollProgram){
        router.navigateTo(Screens.ENROLL_PROGRAM,tmEnrollProgram);
    }

}
