package org.hisp.india.trackercapture.di;

import org.hisp.india.core.di.ActivityScope;
import org.hisp.india.trackercapture.navigator.LocalCiceroneHolder;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nhancao on 4/20/17.
 */

@Module
public class LocalNavigationModule {

    @Provides
    @ActivityScope
    LocalCiceroneHolder provideLocalNavigationHolder() {
        return new LocalCiceroneHolder();
    }
}
