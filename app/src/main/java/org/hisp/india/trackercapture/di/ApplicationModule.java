package org.hisp.india.trackercapture.di;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import org.hisp.india.core.di.ApplicationScope;
import org.hisp.india.core.services.log.DefaultLogService;
import org.hisp.india.core.services.log.LogService;
import org.hisp.india.core.services.network.DefaultNetworkProvider;
import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.BuildConfig;
import org.hisp.india.trackercapture.models.Credentials;
import org.hisp.india.trackercapture.services.account.AccountApi;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.account.DefaultAccountService;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;
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
    public Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationScope
    public LogService provideLogService() {
        DefaultLogService defaultLogService = new DefaultLogService();
        try {
            defaultLogService.init(application, "92565dca-71d0-4a1d-9b53-ec0696eda359");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultLogService;
    }

    @Provides
    @ApplicationScope
    public ApiErrorFilter provideApiErrorFilter(NetworkProvider networkProvider, LogService logService) {
        return new ApiErrorFilter(networkProvider, logService);
    }

    @Provides
    @ApplicationScope
    public Credentials provideCredentials() {
        return Hawk.get(Constants.CREDENTIALS, new Credentials());
    }

    @Provides
    @ApplicationScope
    public NetworkProvider provideNetworkProvider() {
        return new DefaultNetworkProvider(application, BuildConfig.DEBUG);
    }

    @Provides
    @ApplicationScope
    public AccountService provideAccountService(NetworkProvider rxNetworkProvider, Credentials credentials,
                                                ApiErrorFilter apiErrorFilter) {

        AccountApi restService =
                rxNetworkProvider
                        .addDefaultHeader()
                        .addHeader("Authorization", credentials.getApiToken())
                        .provideApi(credentials.getHost(), AccountApi.class);

        return new DefaultAccountService(rxNetworkProvider, restService, credentials, apiErrorFilter);
    }

}
