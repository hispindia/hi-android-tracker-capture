package org.hisp.india.trackercapture.domains.enroll;

import org.hisp.india.core.di.ActivityScope;
import org.hisp.india.trackercapture.di.ApplicationComponent;

import dagger.Component;

/**
 * Created by nhancao on 5/5/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class)
public interface EnrollComponent {
    EnrollPresenter presenter();

    void inject(EnrollActivity enrollActivity);
}
