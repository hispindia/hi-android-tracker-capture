package org.hisp.india.trackercapture.services;

import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.models.base.Credentials;
import org.hisp.india.trackercapture.services.account.AccountApi;
import org.hisp.india.trackercapture.services.account.AccountService;
import org.hisp.india.trackercapture.services.constants.ConstantApi;
import org.hisp.india.trackercapture.services.constants.ConstantService;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentApi;
import org.hisp.india.trackercapture.services.enrollments.EnrollmentService;
import org.hisp.india.trackercapture.services.events.EventApi;
import org.hisp.india.trackercapture.services.events.EventService;
import org.hisp.india.trackercapture.services.optionsets.OptionSetApi;
import org.hisp.india.trackercapture.services.optionsets.OptionSetService;
import org.hisp.india.trackercapture.services.organization.OrganizationApi;
import org.hisp.india.trackercapture.services.organization.OrganizationService;
import org.hisp.india.trackercapture.services.program_rule_actions.ProgramRuleActionApi;
import org.hisp.india.trackercapture.services.program_rule_actions.ProgramRuleActionService;
import org.hisp.india.trackercapture.services.program_rule_variables.ProgramRuleVariableApi;
import org.hisp.india.trackercapture.services.program_rule_variables.ProgramRuleVariableService;
import org.hisp.india.trackercapture.services.program_rules.ProgramRuleApi;
import org.hisp.india.trackercapture.services.program_rules.ProgramRuleService;
import org.hisp.india.trackercapture.services.programs.ProgramApi;
import org.hisp.india.trackercapture.services.programs.ProgramService;
import org.hisp.india.trackercapture.services.relationship_types.RelationshipTypeApi;
import org.hisp.india.trackercapture.services.relationship_types.RelationshipTypeService;
import org.hisp.india.trackercapture.services.tracked_entity_attribute_groups.TrackedEntityAttributeGroupApi;
import org.hisp.india.trackercapture.services.tracked_entity_attribute_groups.TrackedEntityAttributeGroupService;
import org.hisp.india.trackercapture.services.tracked_entity_attributes.TrackedEntityAttributeApi;
import org.hisp.india.trackercapture.services.tracked_entity_attributes.TrackedEntityAttributeService;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceApi;
import org.hisp.india.trackercapture.services.tracked_entity_instances.TrackedEntityInstanceService;

/**
 * Created by nhancao on 6/28/17.
 */

public class RefreshCredentialService {

    private Credentials credentials;
    private NetworkProvider networkProvider;
    private AccountService accountService;
    private OrganizationService organizationService;
    private ConstantService constantService;
    private EnrollmentService enrollmentService;
    private EventService eventService;
    private OptionSetService optionSetService;
    private ProgramService programService;
    private ProgramRuleService programRuleService;
    private ProgramRuleActionService programRuleActionService;
    private ProgramRuleVariableService programRuleVariableService;
    private RelationshipTypeService relationshipTypeService;
    private TrackedEntityInstanceService trackedEntityInstanceService;
    private TrackedEntityAttributeService trackedEntityAttributeService;
    private TrackedEntityAttributeGroupService trackedEntityAttributeGroupService;

    public RefreshCredentialService(Credentials credentials,
                                    NetworkProvider networkProvider,
                                    AccountService accountService,
                                    OrganizationService organizationService,
                                    ConstantService constantService,
                                    EnrollmentService enrollmentService,
                                    EventService eventService,
                                    OptionSetService optionSetService,
                                    ProgramService programService,
                                    ProgramRuleService programRuleService,
                                    ProgramRuleActionService programRuleActionService,
                                    ProgramRuleVariableService programRuleVariableService,
                                    RelationshipTypeService relationshipTypeService,
                                    TrackedEntityInstanceService trackedEntityInstanceService,
                                    TrackedEntityAttributeService trackedEntityAttributeService,
                                    TrackedEntityAttributeGroupService trackedEntityAttributeGroupService) {
        this.credentials = credentials;
        this.networkProvider = networkProvider;
        this.accountService = accountService;
        this.organizationService = organizationService;
        this.constantService = constantService;
        this.enrollmentService = enrollmentService;
        this.eventService = eventService;
        this.optionSetService = optionSetService;
        this.programService = programService;
        this.programRuleService = programRuleService;
        this.programRuleActionService = programRuleActionService;
        this.programRuleVariableService = programRuleVariableService;
        this.relationshipTypeService = relationshipTypeService;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.trackedEntityAttributeService = trackedEntityAttributeService;
        this.trackedEntityAttributeGroupService = trackedEntityAttributeGroupService;
    }

    public void updateCredentialToken() {

        accountService.setRestService(networkProvider
                                              .addDefaultHeader()
                                              .addHeader("Authorization", credentials.getApiToken())
                                              .provideApi(credentials.getHost(), AccountApi.class));

        organizationService.setRestService(networkProvider
                                                   .addDefaultHeader()
                                                   .addHeader("Authorization", credentials.getApiToken())
                                                   .provideApi(credentials.getHost(), OrganizationApi.class));

        constantService.setRestService(networkProvider
                                               .addDefaultHeader()
                                               .addHeader("Authorization", credentials.getApiToken())
                                               .provideApi(credentials.getHost(), ConstantApi.class));

        enrollmentService.setRestService(networkProvider
                                                 .addDefaultHeader()
                                                 .addHeader("Authorization", credentials.getApiToken())
                                                 .provideApi(credentials.getHost(), EnrollmentApi.class));

        eventService.setRestService(networkProvider
                                            .addDefaultHeader()
                                            .addHeader("Authorization", credentials.getApiToken())
                                            .provideApi(credentials.getHost(), EventApi.class));

        optionSetService.setRestService(networkProvider
                                                .addDefaultHeader()
                                                .addHeader("Authorization", credentials.getApiToken())
                                                .provideApi(credentials.getHost(), OptionSetApi.class));

        programService.setRestService(networkProvider
                                              .addDefaultHeader()
                                              .addHeader("Authorization", credentials.getApiToken())
                                              .provideApi(credentials.getHost(), ProgramApi.class));

        programRuleService.setRestService(networkProvider
                                                  .addDefaultHeader()
                                                  .addHeader("Authorization", credentials.getApiToken())
                                                  .provideApi(credentials.getHost(), ProgramRuleApi.class));

        programRuleActionService.setRestService(networkProvider
                                                        .addDefaultHeader()
                                                        .addHeader("Authorization", credentials.getApiToken())
                                                        .provideApi(credentials.getHost(), ProgramRuleActionApi.class));

        programRuleVariableService.setRestService(networkProvider
                                                          .addDefaultHeader()
                                                          .addHeader("Authorization", credentials.getApiToken())
                                                          .provideApi(credentials.getHost(),
                                                                      ProgramRuleVariableApi.class));

        relationshipTypeService.setRestService(networkProvider
                                                       .addDefaultHeader()
                                                       .addHeader("Authorization", credentials.getApiToken())
                                                       .provideApi(credentials.getHost(), RelationshipTypeApi.class));

        trackedEntityInstanceService.setRestService(networkProvider
                                                            .addDefaultHeader()
                                                            .addHeader("Authorization", credentials.getApiToken())
                                                            .provideApi(credentials.getHost(),
                                                                        TrackedEntityInstanceApi.class));

        trackedEntityAttributeService.setRestService(networkProvider
                                                             .addDefaultHeader()
                                                             .addHeader("Authorization", credentials.getApiToken())
                                                             .provideApi(credentials.getHost(),
                                                                         TrackedEntityAttributeApi.class));

        trackedEntityAttributeGroupService.setRestService(networkProvider
                                                                  .addDefaultHeader()
                                                                  .addHeader("Authorization", credentials.getApiToken())
                                                                  .provideApi(credentials.getHost(),
                                                                              TrackedEntityAttributeGroupApi.class));

    }


}
