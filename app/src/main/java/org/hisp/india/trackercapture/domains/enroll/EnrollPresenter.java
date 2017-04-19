package org.hisp.india.trackercapture.domains.enroll;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.hisp.india.core.services.schedulers.RxScheduler;
import org.hisp.india.trackercapture.models.OrganizationUnit;
import org.hisp.india.trackercapture.models.storage.TMapping;
import org.hisp.india.trackercapture.models.storage.TOrganizationUnit;
import org.hisp.india.trackercapture.services.organization.OrganizationService;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 5/5/17.
 */

public class EnrollPresenter extends MvpBasePresenter<EnrollView> {
    private static final String TAG = EnrollPresenter.class.getSimpleName();

    private Router router;

    private OrganizationService organizationService;
    private Realm realm;
    private RealmResults<TOrganizationUnit> tOrganizationUnits;

    @Inject
    public EnrollPresenter(Router router, OrganizationService organizationService) {
        this.router = router;
        this.organizationService = organizationService;
    }

    @Override
    public void attachView(EnrollView view) {
        super.attachView(view);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        realm.close();
    }

    public void getOrganizations() {
        if (tOrganizationUnits == null) {
            tOrganizationUnits = realm.where(TOrganizationUnit.class).findAll();
            tOrganizationUnits.addChangeListener(element -> {
                Log.e(TAG, "getOrganizations:update " + realm.copyFromRealm(tOrganizationUnits));
            });
        }
        Log.e(TAG, "getOrganizations: " + realm.copyFromRealm(tOrganizationUnits));
        getView().showLoading();
        organizationService.getOrganizationUnits()
                           .compose(RxScheduler.applyIoSchedulers())
                           .doOnTerminate(() -> getView().hideLoading())
                           .subscribe(organizationUnitsResponse -> {
                               realm.beginTransaction();
                               for (OrganizationUnit organizationUnit : organizationUnitsResponse
                                       .getOrganizationUnits()) {
                                   TOrganizationUnit tOrganizationUnit = TMapping.from(organizationUnit);
                                   realm.insertOrUpdate(tOrganizationUnit);
                               }
                               realm.commitTransaction();
                           }, Throwable::printStackTrace);


    }

}
