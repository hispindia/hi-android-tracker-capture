package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Attribute;
import org.hisp.india.trackercapture.models.base.DataElement;
import org.hisp.india.trackercapture.models.base.Option;
import org.hisp.india.trackercapture.models.base.OptionSet;
import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.base.Program;
import org.hisp.india.trackercapture.models.base.ProgramRule;
import org.hisp.india.trackercapture.models.base.ProgramRuleAction;
import org.hisp.india.trackercapture.models.base.ProgramRuleVariable;
import org.hisp.india.trackercapture.models.base.ProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.models.base.TrackedEntity;
import org.hisp.india.trackercapture.models.base.TrackedEntityAttribute;
import org.hisp.india.trackercapture.models.base.TrackedEntityInstance;
import org.hisp.india.trackercapture.models.base.User;
import org.hisp.india.trackercapture.models.response.ProgramStage;
import org.hisp.india.trackercapture.models.response.ProgramStageDataElement;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by nhancao on 4/16/17.
 */

public class RMapping {

    public static RUser from(User user) {
        RUser model = new RUser();
        model.setId(user.getId());
        model.setDisplayName(user.getDisplayName());
        model.setEmail(user.getEmail());
        model.setFirstName(user.getFirstName());
        model.setName(user.getName());
        model.setSurName(user.getSurName());
        model.setOrganizationUnits(new RealmList<>());
        for (OrganizationUnit organizationUnit : user.getOrganizationUnits()) {
            ROrganizationUnit item = from(organizationUnit);
            if (item != null) {
                model.getOrganizationUnits().add(item);
            }
        }

        return model;
    }

    public static ROrganizationUnit from(OrganizationUnit organizationUnit) {
        if (organizationUnit == null) return null;
        ROrganizationUnit model = new ROrganizationUnit();
        model.setId(organizationUnit.getId());
        model.setDisplayName(organizationUnit.getDisplayName());
        model.setCode(organizationUnit.getCode());
        model.setLevel(organizationUnit.getLevel());
        model.setPrograms(new RealmList<>());
        if (organizationUnit.getParent() != null) {
            model.setParent(organizationUnit.getParent().getId());
        }
        if (organizationUnit.getPrograms() != null) {
            for (Program program : organizationUnit.getPrograms()) {
                RProgram item = from(program);
                if (item != null) {
                    model.getPrograms().add(item);
                }
            }
        }

        return model;
    }

    public static RDataElement from(DataElement dataElement) {
        if (dataElement == null) return null;
        RDataElement model = new RDataElement();
        model.setId(dataElement.getId());
        model.setDisplayName(dataElement.getDisplayName());
        model.setValueType(dataElement.getValueType());
        model.setOptionSetValue(dataElement.isOptionSetValue());
        model.setOptionSet(from(dataElement.getOptionSet()));
        return model;
    }

    public static RProgramStageDataElement from(ProgramStageDataElement programStageDataElement) {
        if (programStageDataElement == null) return null;
        RProgramStageDataElement model = new RProgramStageDataElement();
        model.setId(programStageDataElement.getId());
        model.setDisplayName(programStageDataElement.getDisplayName());
        model.setDisplayInReports(programStageDataElement.isDisplayInReports());
        model.setCompulsory(programStageDataElement.isCompulsory());
        model.setAllowProvidedElsewhere(programStageDataElement.isAllowProvidedElsewhere());
        model.setSortOrder(programStageDataElement.getSortOrder());
        model.setAllowFutureDate(programStageDataElement.isAllowFutureDate());
        model.setDataElement(from(programStageDataElement.getDataElement()));
        return model;
    }

    public static RProgramStage from(ProgramStage programStage) {
        if (programStage == null) return null;
        RProgramStage model = new RProgramStage();
        model.setId(programStage.getId());
        model.setDisplayName(programStage.getDisplayName());
        model.setSortOrder(programStage.getSortOrder());
        model.setMinDaysFromStart(programStage.getMinDaysFromStart());
        model.setProgramStageDataElements(new RealmList<>());

        if (programStage.getProgramStageDataElements() != null) {
            for (ProgramStageDataElement programStageDataElement : programStage.getProgramStageDataElements()) {
                RProgramStageDataElement item = from(programStageDataElement);
                if (item != null) {
                    model.getProgramStageDataElements().add(item);
                }
            }
        }

        return model;
    }

    public static RProgramRuleVariable from(ProgramRuleVariable programRuleVariable) {
        if (programRuleVariable == null) return null;
        RProgramRuleVariable model = new RProgramRuleVariable();
        model.setId(programRuleVariable.getId());
        model.setDisplayName(programRuleVariable.getDisplayName());
        return model;
    }

    public static ROption from(Option option) {
        if (option == null) return null;
        ROption model = new ROption();
        model.setId(option.getId());
        model.setCode(option.getCode());
        model.setDisplayName(option.getDisplayName());
        return model;
    }

    public static ROptionSet from(OptionSet optionSet) {
        if (optionSet == null) return null;
        ROptionSet model = new ROptionSet();
        model.setId(optionSet.getId());
        model.setDisplayName(optionSet.getDisplayName());
        model.setValueType(optionSet.getValueType());
        model.setOptions(new RealmList<>());
        for (Option option : optionSet.getOptions()) {
            ROption item = from(option);
            if (item != null) {
                model.getOptions().add(item);
            }
        }
        return model;
    }

    public static RTrackedEntityAttribute from(TrackedEntityAttribute trackedEntityAttribute) {
        if (trackedEntityAttribute == null) return null;
        RTrackedEntityAttribute model = new RTrackedEntityAttribute();
        model.setId(trackedEntityAttribute.getId());
        model.setDisplayName(trackedEntityAttribute.getDisplayName());
        model.setOptionSetValue(trackedEntityAttribute.isOptionSetValue());
        ROptionSet optionSet = from(trackedEntityAttribute.getOptionSet());
        if (optionSet != null) {
            model.setOptionSet(optionSet);
        }
        return model;
    }

    public static RProgramTrackedEntityAttribute from(ProgramTrackedEntityAttribute programTrackedEntityAttribute) {
        if (programTrackedEntityAttribute == null) return null;
        RProgramTrackedEntityAttribute model = new RProgramTrackedEntityAttribute();
        model.setId(programTrackedEntityAttribute.getId());
        model.setDisplayName(programTrackedEntityAttribute.getDisplayName());
        model.setMandatory(programTrackedEntityAttribute.isMandatory());
        model.setDisplayInList(programTrackedEntityAttribute.isDisplayInList());
        model.setAllowFutureDate(programTrackedEntityAttribute.isAllowFutureDate());
        model.setValueType(programTrackedEntityAttribute.getValueType());
        RTrackedEntityAttribute trackedEntityAttribute = from(
                programTrackedEntityAttribute.getTrackedEntityAttribute());
        if (trackedEntityAttribute != null) {
            model.setTrackedEntityAttribute(trackedEntityAttribute);
        }
        return model;
    }

    public static RProgramRuleAction from(ProgramRuleAction programRuleAction) {
        if (programRuleAction == null) return null;
        RProgramRuleAction model = new RProgramRuleAction();
        model.setId(programRuleAction.getId());
        model.setDisplayName(programRuleAction.getDisplayName());
        model.setProgramRuleActionType(programRuleAction.getProgramRuleActionType());
        return model;
    }

    public static RProgramRule from(ProgramRule programRule) {
        if (programRule == null) return null;
        RProgramRule model = new RProgramRule();
        model.setId(programRule.getId());
        model.setDisplayName(programRule.getDisplayName());
        model.setCondition(programRule.getCondition());
        model.setDescription(programRule.getDescription());
        model.setProgramRuleActions(new RealmList<>());

        if (programRule.getProgramRuleActions() != null) {
            for (ProgramRuleAction programRuleAction : programRule.getProgramRuleActions()) {
                RProgramRuleAction item = from(programRuleAction);
                if (item != null) {
                    model.getProgramRuleActions().add(item);
                }
            }
        }

        return model;
    }

    public static RTrackedEntity from(TrackedEntity trackedEntity) {
        if (trackedEntity == null) return null;
        RTrackedEntity model = new RTrackedEntity();
        model.setId(trackedEntity.getId());
        model.setDisplayName(trackedEntity.getDisplayName());
        return model;
    }

    public static RProgram from(Program program) {
        if (program == null) return null;
        RProgram model = new RProgram();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        model.setWithoutRegistration(program.isWithoutRegistration());
        model.setEnrollmentDateLabel(program.getEnrollmentDateLabel());
        model.setIncidentDateLabel(program.getIncidentDateLabel());
        model.setSelectEnrollmentDatesInFuture(program.isSelectEnrollmentDatesInFuture());
        model.setSelectIncidentDatesInFuture(program.isSelectIncidentDatesInFuture());
        model.setDisplayIncidentDate(program.isDisplayIncidentDate());
        if (program.getTrackedEntity() != null) {
            model.setTrackedEntity(from(program.getTrackedEntity()));
        }
        model.setProgramStages(new RealmList<>());
        model.setProgramRuleVariables(new RealmList<>());
        model.setProgramRules(new RealmList<>());
        model.setProgramTrackedEntityAttributes(new RealmList<>());

        if (program.getProgramStages() != null) {
            for (ProgramStage programStage : program.getProgramStages()) {
                RProgramStage item = from(programStage);
                if (item != null) {
                    model.getProgramStages().add(item);
                }
            }
        }

        if (program.getProgramRuleVariables() != null) {
            for (ProgramRuleVariable programRuleVariable : program.getProgramRuleVariables()) {
                RProgramRuleVariable item = from(programRuleVariable);
                if (item != null) {
                    model.getProgramRuleVariables().add(item);
                }
            }
        }

        if (program.getProgramTrackedEntityAttributes() != null) {
            for (ProgramTrackedEntityAttribute programTrackedEntityAttribute : program
                    .getProgramTrackedEntityAttributes()) {
                RProgramTrackedEntityAttribute item = from(programTrackedEntityAttribute);
                if (item != null) {
                    model.getProgramTrackedEntityAttributes().add(item);
                }
            }
        }

        if (program.getProgramRules() != null) {
            for (ProgramRule programRule : program.getProgramRules()) {
                RProgramRule item = from(programRule);
                if (item != null) {
                    model.getProgramRules().add(item);
                }
            }
        }
        return model;
    }

    public static RAttribute from(Attribute attribute) {
        if (attribute == null) return null;
        RAttribute model = new RAttribute();
        model.setAttributeId(attribute.getAttributeId());
        model.setDisplayName(attribute.getDisplayName());
        model.setValue(attribute.getValue());
        model.setValueType(attribute.getValueType());
        return model;
    }

    public static Attribute from(RAttribute attribute) {
        if (attribute == null) return null;
        Attribute model = new Attribute();
        model.setAttributeId(attribute.getAttributeId());
        model.setDisplayName(attribute.getDisplayName());
        model.setValue(attribute.getValue());
        model.setValueType(attribute.getValueType());
        return model;
    }

    public static RTrackedEntityInstance from(TrackedEntityInstance trackedEntityInstance) {
        if (trackedEntityInstance == null) return null;
        RTrackedEntityInstance model = new RTrackedEntityInstance();
        model.setTrackedEntityInstanceId(trackedEntityInstance.getTrackedEntityInstanceId());
        model.setTrackedEntityId(trackedEntityInstance.getTrackedEntityId());
        model.setOrgUnitId(trackedEntityInstance.getOrgUnitId());
        model.setProgramId(trackedEntityInstance.getProgramId());

        RealmList<RAttribute> attributes = new RealmList<>();

        if (trackedEntityInstance.getAttributeList() != null) {
            for (Attribute attribute : trackedEntityInstance.getAttributeList()) {
                if (attribute != null) attributes.add(from(attribute));
            }
        }
        model.setAttributeList(attributes);

        return model;
    }

    public static TrackedEntityInstance from(RTrackedEntityInstance trackedEntityInstance) {
        if (trackedEntityInstance == null) return null;
        TrackedEntityInstance model = new TrackedEntityInstance();
        model.setTrackedEntityInstanceId(trackedEntityInstance.getTrackedEntityInstanceId());
        model.setTrackedEntityId(trackedEntityInstance.getTrackedEntityId());
        model.setOrgUnitId(trackedEntityInstance.getOrgUnitId());
        model.setProgramId(trackedEntityInstance.getProgramId());

        List<Attribute> attributes = new ArrayList<>();
        if (trackedEntityInstance.getAttributeList() != null) {
            for (RAttribute attribute : trackedEntityInstance.getAttributeList()) {
                if (attribute != null) attributes.add(from(attribute));
            }
        }
        model.setAttributeList(attributes);

        return model;
    }

}

