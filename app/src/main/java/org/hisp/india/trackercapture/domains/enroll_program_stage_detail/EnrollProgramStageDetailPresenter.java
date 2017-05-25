package org.hisp.india.trackercapture.domains.enroll_program_stage_detail;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.storage.RProgramStageDataElement;

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
    }

    @Override
    public void detachView(boolean retainInstance) {
        navigatorHolder.removeNavigator();
        super.detachView(retainInstance);
    }

    public void onBackCommandClick() {
        router.exit();
    }

    public void getProgramStageDetail(List<RProgramStageDataElement> programStageDataElements) {
        if (isViewAttached()) {
            Observable
                    .defer(() -> Observable.just(programStageDataElements)
                                           .delay(500, TimeUnit.MILLISECONDS)
                                           .compose(RxScheduler.applyLogicSchedulers())
                                           .doOnSubscribe(() -> getView().showLoading())
                                           .doOnTerminate(() -> getView().hideLoading()))
                    .subscribe(programStageDataElementList -> getView()
                            .getProgramStageDetail(programStageDataElementList));

        }
    }

}
