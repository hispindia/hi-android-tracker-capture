package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.base.Program;
import org.hisp.india.trackercapture.models.base.User;

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
            model.getOrganizationUnits().add(RMapping.from(organizationUnit));
        }

        return model;
    }

    public static ROrganizationUnit from(OrganizationUnit organizationUnit) {
        ROrganizationUnit model = new ROrganizationUnit();
        model.setId(organizationUnit.getId());
        model.setDisplayName(organizationUnit.getDisplayName());
        model.setPrograms(new RealmList<>());

        for (Program program : organizationUnit.getPrograms()) {
            model.getPrograms().add(from(program));
        }

        return model;
    }

    public static RProgram from(Program program) {
        RProgram model = new RProgram();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static RConstant from(RConstant program) {
        RConstant model = new RConstant();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static ROptionSet from(ROptionSet program) {
        ROptionSet model = new ROptionSet();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static RProgramRule from(RProgramRule program) {
        RProgramRule model = new RProgramRule();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static RProgramRuleAction from(RProgramRuleAction program) {
        RProgramRuleAction model = new RProgramRuleAction();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static RProgramRuleVariable from(RProgramRuleVariable program) {
        RProgramRuleVariable model = new RProgramRuleVariable();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static RRelationshipType from(RRelationshipType program) {
        RRelationshipType model = new RRelationshipType();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static RTrackedEntityAttribute from(RTrackedEntityAttribute program) {
        RTrackedEntityAttribute model = new RTrackedEntityAttribute();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static RTrackedEntityAttributeGroup from(RTrackedEntityAttributeGroup program) {
        RTrackedEntityAttributeGroup model = new RTrackedEntityAttributeGroup();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

    public static RTrackedEntityInstance from(RTrackedEntityInstance program) {
        RTrackedEntityInstance model = new RTrackedEntityInstance();
        model.setId(program.getId());
        model.setDisplayName(program.getDisplayName());
        return model;
    }

}
