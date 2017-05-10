package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Option;
import org.hisp.india.trackercapture.models.base.OptionSet;
import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.base.Program;
import org.hisp.india.trackercapture.models.base.ProgramRule;
import org.hisp.india.trackercapture.models.base.ProgramRuleVariable;
import org.hisp.india.trackercapture.models.base.ProgramTrackedEntityAttribute;
import org.hisp.india.trackercapture.models.base.TrackedEntity;
import org.hisp.india.trackercapture.models.base.TrackedEntityAttribute;
import org.hisp.india.trackercapture.models.base.User;
import org.hisp.india.trackercapture.models.response.ProgramStage;

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
        model.setPrograms(new RealmList<>());

        for (Program program : organizationUnit.getPrograms()) {
            RProgram item = from(program);
            if (item != null) {
                model.getPrograms().add(item);
            }
        }

        return model;
    }

    public static RProgramStage from(ProgramStage programStage) {
        if (programStage == null) return null;
        RProgramStage model = new RProgramStage();
        model.setId(programStage.getId());
        model.setDisplayName(programStage.getDisplayName());
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

    public static RProgramRule from(ProgramRule programRule) {
        if (programRule == null) return null;
        RProgramRule model = new RProgramRule();
        model.setId(programRule.getId());
        model.setDisplayName(programRule.getDisplayName());
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
        TrackedEntity trackedEntity = program.getTrackedEntity();
        if (trackedEntity != null) {
            model.setTrackedEntity(from(trackedEntity));
        }
        model.setProgramStages(new RealmList<>());
        model.setProgramRuleVariables(new RealmList<>());
        model.setProgramRules(new RealmList<>());
        model.setProgramTrackedEntityAttributes(new RealmList<>());

        for (ProgramStage programStage : program.getProgramStages()) {
            RProgramStage item = from(programStage);
            if (item != null) {
                model.getProgramStages().add(item);
            }
        }

        for (ProgramRuleVariable programRuleVariable : program.getProgramRuleVariables()) {
            RProgramRuleVariable item = from(programRuleVariable);
            if (item != null) {
                model.getProgramRuleVariables().add(item);
            }
        }

        for (ProgramTrackedEntityAttribute programTrackedEntityAttribute : program
                .getProgramTrackedEntityAttributes()) {
            RProgramTrackedEntityAttribute item = from(programTrackedEntityAttribute);
            if (item != null) {
                model.getProgramTrackedEntityAttributes().add(item);
            }
        }

        for (ProgramRule programRule : program.getProgramRules()) {
            RProgramRule item = from(programRule);
            if (item != null) {
                model.getProgramRules().add(item);
            }
        }
        return model;
    }

}