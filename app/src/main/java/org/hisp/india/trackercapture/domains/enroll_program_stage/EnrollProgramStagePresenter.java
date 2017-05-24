package org.hisp.india.trackercapture.domains.enroll_program_stage;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.nhancv.ntask.NTaskManager;
import com.nhancv.ntask.RTask;

import org.greenrobot.eventbus.Subscribe;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.Event;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.TaskRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.models.storage.RProgramStage;
import org.hisp.india.trackercapture.navigator.Screens;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;
import org.hisp.india.trackercapture.services.task.BusProgress;
import org.hisp.india.trackercapture.services.task.TaskService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Observable;

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
                    getView().showLoading();
                    break;
                case FINISH:
                    getView().hideLoading();
                    break;
                case SUCCESS:
                    getView().registerProgramSuccess();
                    router.exit();
                    break;
            }
        }
    }

    public void onBackCommandClick() {
        router.exit();
    }

    public void getProgramDetail(String programId) {
        if (isViewAttached()) {
            Observable
                    .defer(() -> Observable.just(ProgramQuery.getProgram(programId))
                                           .delay(500, TimeUnit.MILLISECONDS)
                                           .compose(RxScheduler.applyLogicSchedulers())
                                           .doOnSubscribe(() -> getView().showLoading())
                                           .doOnTerminate(() -> getView().hideLoading()))
                    .subscribe(program -> getView().getProgramDetail(program));

        }
    }

    public void registerProgram(TrackedEntityInstanceRequest trackedEntityInstanceRequest,
                                EnrollmentRequest enrollmentRequest, List<Event> eventList) {

        NTaskManager
                .postTask(RTask.build(UUID.randomUUID().toString(), TAG, new TaskRequest(trackedEntityInstanceRequest,
                                                                                         enrollmentRequest,
                                                                                         eventList).toString()));

    }

    public void openProgramStage(RProgramStage programStage) {
        ProgramQuery.saveProgramStage(programStage);
        router.navigateTo(Screens.ENROLL_PROGRAM_STAGE_DETAIL, programStage.getId());
    }

}
