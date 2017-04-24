package org.hisp.india.trackercapture.domains.enroll;

import org.hisp.india.core.di.module.LocalNavigationModule;
import org.hisp.india.core.di.module.NavigationModule;
import org.hisp.india.core.di.scope.ActivityScope;
import org.hisp.india.trackercapture.di.ApplicationComponent;

import dagger.Component;

/**
 * Created by nhancao on 5/5/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                NavigationModule.class,
                LocalNavigationModule.class
        })
public interface EnrollComponent {
    EnrollPresenter presenter();

    void inject(EnrollActivity enrollActivity);
}
