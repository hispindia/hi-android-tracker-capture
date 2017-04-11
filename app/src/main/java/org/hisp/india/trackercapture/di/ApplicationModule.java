package org.hisp.india.trackercapture.di;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import org.hisp.india.core.di.ApplicationScope;
import org.hisp.india.core.services.log.DefaultLogService;
import org.hisp.india.core.services.log.LogService;
import org.hisp.india.core.services.network.DefaultRxNetworkProvider;
import org.hisp.india.core.services.network.RxNetworkProvider;
import org.hisp.india.trackercapture.BuildConfig;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.services.account.AccountApi;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.account.DefaultAccountService;
import org.hisp.india.trackercapture.utils.Constants;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nhancao on 5/5/17.
 */
@Module
public class ApplicationModule {
    private static final String TAG = ApplicationModule.class.getSimpleName();

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationScope
    LogService provideLogService() {
        DefaultLogService defaultLogService = new DefaultLogService();
        try {
            defaultLogService.init(application, false, true, false, null, 0, "92565dca-71d0-4a1d-9b53-ec0696eda359", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultLogService;
    }

    @Provides
    @ApplicationScope
    Credentials provideCredentials() {
        return Hawk.get(Constants.CREDENTIALS, new Credentials());
    }

    @Provides
    @ApplicationScope
    RxNetworkProvider provideNetworkProvider() {
        return new DefaultRxNetworkProvider(application, BuildConfig.DEBUG);
    }

    @Provides
    @ApplicationScope
    AccountService provideAccountService(RxNetworkProvider rxNetworkProvider, Credentials credentials) {

        AccountApi restService =
                rxNetworkProvider
                        .addDefaultHeader()
                        .addHeader("Authorization", credentials.getApiToken())
                        .provideApi(credentials.getHost(), AccountApi.class);

        return new DefaultAccountService(rxNetworkProvider, restService, credentials);
    }

}
