package org.hisp.india.trackercapture.domains.enroll.step1;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.hisp.india.trackercapture.MainApplication;
import org.hisp.india.trackercapture.R;
import org.hisp.india.trackercapture.domains.enroll.EnrollActivity;
import org.hisp.india.trackercapture.models.storage.TOrganizationUnit;
import org.hisp.india.trackercapture.widgets.autocomplete.NAutoCompleteTextView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nhancao on 4/20/17.
 */

@EFragment(R.layout.fragment_step1)
public class Step1Fragment extends MvpFragment<Step1View, Step1Presenter> implements Step1View {
    private static final String TAG = Step1Fragment.class.getSimpleName();

    @ViewById(R.id.fragment_step1_at_org)
    NAutoCompleteTextView<TOrganizationUnit> atOrg;

    @App
    MainApplication application;
    @Inject
    Step1Presenter presenter;

    EnrollActivity activity;

    public static Step1Fragment getNewInstance() {
        return new Step1Fragment_();
    }

    @AfterInject
    void inject() {
        DaggerStep1Component.builder()
                            .applicationComponent(application.getApplicationComponent())
                            .build()
                            .inject(this);
    }

    @AfterViews
    void init() {

        activity = (EnrollActivity) getActivity();
        presenter.getOrganizations();

    }

    @Override
    public Step1Presenter createPresenter() {
        return presenter;
    }


    @Override
    public void showLoading() {
        activity.showProgressLoading();
    }

    @Override
    public void hideLoading() {
        activity.hideProgressLoading();
    }

    @Override
    public void showOrgList(List<TOrganizationUnit> organizationUnitList) {
        atOrg.setModelList(organizationUnitList);
    }

}
