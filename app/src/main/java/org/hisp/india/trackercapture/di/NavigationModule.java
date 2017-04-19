package org.hisp.india.trackercapture.di;

import org.hisp.india.core.di.ActivityScope;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by nhancao on 4/20/17.
 */

@Module
public class NavigationModule {
    private Cicerone<Router> cicerone;

    public NavigationModule() {
        cicerone = Cicerone.create();
    }

    @Provides
    @ActivityScope
    Router provideRouter() {
        return cicerone.getRouter();
    }

    @Provides
    @ActivityScope
    NavigatorHolder provideNavigatorHolder() {
        return cicerone.getNavigatorHolder();
    }
}
