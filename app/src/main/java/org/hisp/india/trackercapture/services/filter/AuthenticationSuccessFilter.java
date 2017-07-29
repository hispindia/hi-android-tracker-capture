package org.hisp.india.trackercapture.services.filter;

import org.hisp.india.core.services.filter.OutputFilter;
import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.base.Program;
import org.hisp.india.trackercapture.models.base.User;
import org.hisp.india.trackercapture.models.storage.RMapping;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by nhancao on 4/16/17.
 */

public class AuthenticationSuccessFilter
        implements OutputFilter<Observable.Transformer<User, User>> {
    private static final String TAG = AuthenticationSuccessFilter.class.getSimpleName();
    private Credentials credentials;

    public AuthenticationSuccessFilter(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Observable.Transformer<User, User> execute() {
        return userObservable -> userObservable
                .observeOn(Schedulers.io())
                .map(user -> {
                    for (OrganizationUnit organizationUnit : user.getOrganizationUnits()) {
                        List<Program> programList = new ArrayList<>();
                        for (Program program : organizationUnit.getPrograms()) {
                            if (!program.isWithoutRegistration()) {
                                programList.add(program);
                            }
                        }
                        organizationUnit.setPrograms(programList);
                    }
                    return user;
                })
                .flatMap(user -> {
                    RealmHelper.transaction(realm -> realm.copyToRealmOrUpdate(RMapping.from(user)));
                    credentials.setLoginSuccess(true);
                    return Observable.just(user);
                });
    }
}
