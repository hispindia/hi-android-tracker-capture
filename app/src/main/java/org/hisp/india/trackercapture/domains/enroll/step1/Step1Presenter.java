package org.hisp.india.trackercapture.domains.enroll.step1;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.storage.TMapping;
import org.hisp.india.trackercapture.models.storage.TOrganizationUnit;
import org.hisp.india.trackercapture.services.organization.OrganizationService;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.terrakok.cicerone.Router;
import rx.Observable;

/**
 * Created by nhancao on 4/20/17.
 */

public class Step1Presenter extends MvpBasePresenter<Step1View> {

    private static final String TAG = Step1Presenter.class.getSimpleName();

    private Router router;

    private OrganizationService organizationService;
    private Realm realm;
    private RealmResults<TOrganizationUnit> tOrganizationUnits;

    @Inject
    public Step1Presenter(Router router, OrganizationService organizationService) {
        this.router = router;
        this.organizationService = organizationService;
    }

    @Override
    public void attachView(Step1View view) {
        super.attachView(view);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void detachView(boolean retainInstance) {
        realm.close();
        super.detachView(retainInstance);
    }


    public void getOrganizations() {
        if (tOrganizationUnits == null) {
            tOrganizationUnits = realm.where(TOrganizationUnit.class).findAll();
            tOrganizationUnits.addChangeListener(element -> {
                getView().showOrgList(realm.copyFromRealm(tOrganizationUnits));
            });
        }
        getView().showOrgList(realm.copyFromRealm(tOrganizationUnits));
        getView().showLoading();
        organizationService.getOrganizationUnits()
                           .compose(RxScheduler.applyIoSchedulers())
                           .doOnTerminate(() -> getView().hideLoading())
                           .subscribe(organizationUnitsResponse -> {
                               realm.beginTransaction();
                               Observable.from(organizationUnitsResponse.getOrganizationUnits())
                                         .map(TMapping::from)
                                         .toList()
                                         .map(organizationUnitList -> {
                                             realm.insertOrUpdate(organizationUnitList);
                                             return organizationUnitList;
                                         }).subscribe();
                               realm.commitTransaction();
                           }, Throwable::printStackTrace);


    }

}
