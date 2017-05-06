package org.hisp.india.trackercapture.domains.enroll;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.services.programs.ProgramService;

import javax.inject.Inject;

import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 5/5/17.
 */

public class EnrollPresenter extends MvpBasePresenter<EnrollView> {
    private static final String TAG = EnrollPresenter.class.getSimpleName();

    private NavigatorHolder navigatorHolder;
    private Router router;
    private ProgramService programService;

    @Inject
    public EnrollPresenter(Router router, NavigatorHolder navigatorHolder, ProgramService programService) {
        this.router = router;
        this.navigatorHolder = navigatorHolder;
        this.programService = programService;
    }

    @Override
    public void attachView(EnrollView view) {
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

    public void getProgramDetail(String programId) {
        getView().showLoading();
        programService.getProgramDetail(programId)
                      .compose(RxScheduler.applyIoSchedulers())
                      .doOnTerminate(() -> getView().hideLoading())
                      .subscribe(programsResponse -> {
                          getView().getProgramDetailSuccess(programsResponse);
                      });
    }


}
