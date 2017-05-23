package org.hisp.india.trackercapture.domains.enroll_program_stage_detail;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.base.Event;
import org.hisp.india.trackercapture.models.request.EnrollmentRequest;
import org.hisp.india.trackercapture.models.request.TrackedEntityInstanceRequest;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;
import org.hisp.india.trackercapture.services.task.BusProgress;
import org.hisp.india.trackercapture.services.task.TaskService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import rx.Observable;

/**
 * Created by nhancao on 5/5/17.
 */

public class EnrollProgramStageDetailPresenter extends MvpBasePresenter<EnrollProgramStageDetailView> {
    private static final String TAG = EnrollProgramStageDetailPresenter.class.getSimpleName();

    private NavigatorHolder navigatorHolder;
    private Router router;

    @Inject
    public EnrollProgramStageDetailPresenter(Router router, NavigatorHolder navigatorHolder) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
    }

    @Override
    public void attachView(EnrollProgramStageDetailView view) {
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


    }

}
