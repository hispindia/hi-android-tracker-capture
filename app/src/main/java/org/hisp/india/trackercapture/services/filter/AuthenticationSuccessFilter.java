package org.hisp.india.trackercapture.services.filter;

import com.orhanobut.hawk.Hawk;

import org.hisp.india.core.services.filter.OutputFilter;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.models.User;
import org.hisp.india.trackercapture.utils.Constants;

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
                    credentials.setUserInfo(user);
                    Hawk.put(Constants.CREDENTIALS, credentials);
                    return Observable.just(user);
                });
    }
}
