package org.hisp.india.trackercapture.di;

import android.app.Application;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.hisp.india.core.di.scope.ApplicationScope;
import org.hisp.india.core.services.log.DefaultLogService;
import org.hisp.india.core.services.log.LogService;
import org.hisp.india.core.services.network.DefaultNetworkProvider;
import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.BuildConfig;
import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.services.account.AccountApi;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.account.DefaultAccountService;
import org.hisp.india.trackercapture.services.constants.ConstantApi;
import org.hisp.india.trackercapture.services.constants.ConstantService;
import org.hisp.india.trackercapture.services.constants.DefaultConstantService;
import org.hisp.india.trackercapture.services.enrollments.DefaultEnrollmentService;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentApi;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.events.DefaultEventService;
import org.hisp.india.trackercapture.services.events.EventApi;
import org.hisp.india.trackercapture.services.events.EventService;
import org.hisp.india.trackercapture.services.filter.ApiErrorFilter;
import org.hisp.india.trackercapture.services.optionsets.DefaultOptionSetService;
import org.hisp.india.trackercapture.services.optionsets.OptionSetApi;
import org.hisp.india.trackercapture.services.optionsets.OptionSetService;
import org.hisp.india.trackercapture.services.organization.DefaultOrganizationService;
import org.hisp.india.trackercapture.services.organization.OrganizationApi;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.services.program_rule_actions.DefaultProgramRuleActionService;
import org.hisp.india.trackercapture.services.program_rule_actions.ProgramRuleActionApi;
import org.hisp.india.trackercapture.services.program_rule_actions.ProgramRuleActionService;
import org.hisp.india.trackercapture.services.program_rule_variables.DefaultProgramRuleVariableService;
import org.hisp.india.trackercapture.services.program_rule_variables.ProgramRuleVariableApi;
import org.hisp.india.trackercapture.services.program_rule_variables.ProgramRuleVariableService;
import org.hisp.india.trackercapture.services.program_rules.DefaultProgramRuleService;
import org.hisp.india.trackercapture.services.program_rules.ProgramRuleApi;
import org.hisp.india.trackercapture.services.program_rules.ProgramRuleService;
import org.hisp.india.trackercapture.services.programs.DefaultProgramService;
import org.hisp.india.trackercapture.services.programs.ProgramApi;
import org.hisp.india.trackercapture.services.programs.ProgramService;
import org.hisp.india.trackercapture.services.relationship_types.DefaultRelationshipTypeService;
import org.hisp.india.trackercapture.services.relationship_types.RelationshipTypeApi;
import org.hisp.india.trackercapture.services.relationship_types.RelationshipTypeService;
import org.hisp.india.trackercapture.services.sync.DefaultSyncService;
import org.hisp.india.trackercapture.services.sync.SyncService;
import org.hisp.india.trackercapture.services.tracked_entity_attribute_groups.DefaultTrackedEntityAttributeGroupService;
import org.hisp.india.trackercapture.services.tracked_entity_attribute_groups.TrackedEntityAttributeGroupApi;
import org.hisp.india.trackercapture.services.tracked_entity_attribute_groups.TrackedEntityAttributeGroupService;
import org.hisp.india.trackercapture.services.tracked_entity_attributes.DefaultTrackedEntityAttributeService;
import org.hisp.india.trackercapture.services.tracked_entity_attributes.TrackedEntityAttributeApi;
import org.hisp.india.trackercapture.services.tracked_entity_attributes.TrackedEntityAttributeService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.DefaultTrackedEntityInstanceService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceApi;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;
import org.hisp.india.trackercapture.utils.Constants;
import org.hisp.india.trackercapture.utils.MapDeserializer;

import java.io.IOException;
import java.util.Map;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmObject;

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
    public Credentials provideCredentials() {
        return Hawk.get(Constants.CREDENTIALS, new Credentials());
    }

    @Provides
    @ApplicationScope
    public NetworkProvider provideNetworkProvider(LogService logService, Credentials credentials) {
        NetworkProvider networkProvider = new DefaultNetworkProvider(application, BuildConfig.DEBUG) {
            @Override
            public GsonBuilder createBuilder() {
                return super.createBuilder()
                            .setExclusionStrategies(new ExclusionStrategy() {
                                @Override
                                public boolean shouldSkipField(FieldAttributes f) {
                                    return f.getDeclaringClass().equals(RealmObject.class);
                                }

                                @Override
                                public boolean shouldSkipClass(Class<?> clazz) {
                                    return false;
                                }
                            })
                            .registerTypeAdapter(new TypeToken<Map<String, String>>() {}.getType(),
                                                 new MapDeserializer());
            }
        };
        return networkProvider.addDefaultHeader()
                              .enableFilter(true)
                              .addHeader("Authorization", credentials.getApiToken())
                              .addFilter(new ApiErrorFilter(networkProvider, logService));
    }

    @Provides
    @ApplicationScope
    public AccountService provideAccountService(NetworkProvider rxNetworkProvider, Credentials credentials) {

        AccountApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), AccountApi.class, true);

        return new DefaultAccountService(rxNetworkProvider, restService, credentials);
    }

    @Provides
    @ApplicationScope
    public OrganizationService provideOrganizationService(NetworkProvider rxNetworkProvider, Credentials credentials) {

        OrganizationApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), OrganizationApi.class, true);

        return new DefaultOrganizationService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public ProgramService provideProgramService(NetworkProvider rxNetworkProvider, Credentials credentials) {

        ProgramApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), ProgramApi.class, true);

        return new DefaultProgramService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public TrackedEntityInstanceService provideTrackedEntityInstanceService(NetworkProvider rxNetworkProvider,
                                                                            Credentials credentials) {

        TrackedEntityInstanceApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), TrackedEntityInstanceApi.class, true);

        return new DefaultTrackedEntityInstanceService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public EnrollmentService provideEnrollmentService(NetworkProvider rxNetworkProvider, Credentials credentials) {

        EnrollmentApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), EnrollmentApi.class, true);

        return new DefaultEnrollmentService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public EventService provideEventService(NetworkProvider rxNetworkProvider, Credentials credentials) {

        EventApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), EventApi.class, true);

        return new DefaultEventService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public ConstantService provideConstantService(NetworkProvider rxNetworkProvider, Credentials credentials) {

        ConstantApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), ConstantApi.class, true);

        return new DefaultConstantService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public OptionSetService provideOptionSetService(NetworkProvider rxNetworkProvider, Credentials credentials) {

        OptionSetApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), OptionSetApi.class, true);

        return new DefaultOptionSetService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public ProgramRuleService provideProgramRuleService(NetworkProvider rxNetworkProvider, Credentials credentials) {

        ProgramRuleApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), ProgramRuleApi.class, true);

        return new DefaultProgramRuleService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public ProgramRuleActionService provideProgramRuleActionService(NetworkProvider rxNetworkProvider,
                                                                    Credentials credentials) {

        ProgramRuleActionApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), ProgramRuleActionApi.class, true);

        return new DefaultProgramRuleActionService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public ProgramRuleVariableService provideProgramRuleVariableService(NetworkProvider rxNetworkProvider,
                                                                        Credentials credentials) {

        ProgramRuleVariableApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), ProgramRuleVariableApi.class, true);

        return new DefaultProgramRuleVariableService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public RelationshipTypeService provideRelationshipTypeService(NetworkProvider rxNetworkProvider,
                                                                  Credentials credentials) {

        RelationshipTypeApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), RelationshipTypeApi.class, true);

        return new DefaultRelationshipTypeService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public TrackedEntityAttributeService provideTrackedEntityAttributeService(NetworkProvider rxNetworkProvider,
                                                                              Credentials credentials) {

        TrackedEntityAttributeApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), TrackedEntityAttributeApi.class, true);

        return new DefaultTrackedEntityAttributeService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public TrackedEntityAttributeGroupService provideTrackedEntityAttributeGroupService(
            NetworkProvider rxNetworkProvider, Credentials credentials) {

        TrackedEntityAttributeGroupApi restService =
                rxNetworkProvider
                        .provideApi(credentials.getHost(), TrackedEntityAttributeGroupApi.class, true);

        return new DefaultTrackedEntityAttributeGroupService(rxNetworkProvider, restService);
    }

    @Provides
    @ApplicationScope
    public SyncService provideSyncService() {
        return new DefaultSyncService();
    }


}
