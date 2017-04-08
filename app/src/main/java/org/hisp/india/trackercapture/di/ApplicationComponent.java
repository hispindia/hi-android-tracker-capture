package org.hisp.india.trackercapture.di;

import org.hisp.india.core.di.ApplicationScope;
import org.hisp.india.core.services.log.LogService;
import org.hisp.india.core.services.network.RxNetworkProvider;
import org.hisp.india.trackercapture.services.account.AccountService;

import dagger.Component;

/**
 * Created by nhancao on 5/5/17.
 */

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    LogService logService();

    RxNetworkProvider networkProvider();

    AccountService accountService();
}
