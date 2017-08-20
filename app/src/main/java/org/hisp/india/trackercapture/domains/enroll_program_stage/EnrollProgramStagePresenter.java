package org.hisp.india.trackercapture.domains.enroll_program_stage;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskEvent;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.storage.RTaskTrackedEntityInstance;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.task.BusProgress;
import org.hisp.india.trackercapture.services.task.TaskService;

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
        TaskService.bus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        TaskService.bus.unregister(this);
        super.detachView(retainInstance);
    }

    @Subscribe
    public void taskBusSubscribe(BusProgress busProgress) {
        if (isViewAttached()) {
            switch (busProgress) {
                case LOADING:
                    getView().showToastMessage("Process ...");
                    break;
                case FINISH:
                    break;
                case SUCCESS:
                    getView().registerProgramSuccess();
                    router.exit();
                    break;
                case ERROR:
                    getView().showToastMessage("Something error");
                    router.exit();
                    break;
            }
        }
    }

    public void onBackCommandClick() {
        router.exit();
    }

    public void registerProgram(RTaskTrackedEntityInstance trackedEntityInstance,
                                RTaskEnrollment enrollment, List<RTaskEvent> eventList) {
        System.out.println(trackedEntityInstance.getAttributeRequestList());
//        NTaskManager
//                .postTask(RTask.build(UUID.randomUUID().toString(), TAG, new TaskRequest(trackedEntityInstanceRequest,
//                                                                                         enrollmentRequest,
//                                                                                         eventList).toString()));

        RTaskRequest taskRequest = RTaskRequest.create(trackedEntityInstance, enrollment, eventList);
        taskRequest.save();
    }

    public void openProgramStage(RProgramStage programStage) {
        router.navigateTo(Screens.ENROLL_PROGRAM_STAGE_DETAIL, programStage);
    }

}
