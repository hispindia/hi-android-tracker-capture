package org.hisp.india.trackercapture.services.account;

import org.hisp.india.trackercapture.models.User;

import rx.Observable;

/**
 * Created by nhancao on 4/9/17.
 */

public interface AccountService {

    void updateCredential(String host, String apiToken);

    Observable<User> login();

}
