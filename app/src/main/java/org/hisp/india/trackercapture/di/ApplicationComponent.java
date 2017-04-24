package org.hisp.india.trackercapture.di;

import org.hisp.india.core.di.scope.ApplicationScope;
import org.hisp.india.core.services.log.LogService;
import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.services.programs.ProgramService;

import dagger.Component;

/**
 * Created by nhancao on 5/5/17.
 */

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    LogService logService();

    NetworkProvider networkProvider();

    ApiErrorFilter apiErrorFilter();

    AccountService accountService();

    OrganizationService organizationService();

    ProgramService programService();
}
