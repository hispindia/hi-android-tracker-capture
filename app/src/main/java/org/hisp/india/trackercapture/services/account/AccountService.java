package org.hisp.india.trackercapture.services.account;

import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.models.base.User;

import rx.Observable;

/**
 * Created by nhancao on 4/9/17.
 */

public interface AccountService {

    Credentials getCredentials();

    void updateCredentialHost(String host);

    void updateCredentialToken(String username, String password);

    Observable<User> login();

    void logout();

    boolean isLogin();

}
