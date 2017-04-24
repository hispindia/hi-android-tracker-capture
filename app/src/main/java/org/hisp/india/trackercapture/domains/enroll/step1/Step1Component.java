package org.hisp.india.trackercapture.domains.enroll.step1;

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
                NavigationModule.class
        })
public interface Step1Component {
    Step1Presenter presenter();

    void inject(Step1Fragment step1Fragment);
}
