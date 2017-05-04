package org.hisp.india.trackercapture.services.filter;

import org.hisp.india.core.services.filter.OutputFilter;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.models.base.User;
import org.hisp.india.trackercapture.models.storage.RMapping;
import org.hisp.india.trackercapture.utils.RealmHelper;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by nhancao on 4/16/17.
 */

public class AuthenticationSuccessFilter
        implements OutputFilter<Observable.Transformer<User, User>> {

    private Credentials credentials;

    public AuthenticationSuccessFilter(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Observable.Transformer<User, User> execute() {
        return userObservable -> userObservable
                .observeOn(Schedulers.computation())
                .flatMap(user -> {

                    RealmHelper.transaction(realm -> {
                        realm.copyToRealmOrUpdate(RMapping.from(user));
                    });

                    credentials.setLoginSuccess(true);
                    return Observable.just(user);
                });
    }
}
