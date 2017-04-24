package org.hisp.india.trackercapture.domains.main;

import org.hisp.india.core.di.scope.ActivityScope;
import org.hisp.india.trackercapture.di.ApplicationComponent;

import dagger.Component;

/**
 * Created by nhancao on 5/5/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class)
public interface MainComponent {
    MainPresenter presenter();

    void inject(MainActivity mainActivity);
}
