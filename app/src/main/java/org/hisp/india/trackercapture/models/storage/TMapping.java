package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.OrganizationUnit;
import org.hisp.india.trackercapture.models.base.Program;

/**
 * Created by nhancao on 4/16/17.
 */

public class TMapping {

    public static TOrganizationUnit from(OrganizationUnit organizationUnit) {
        TOrganizationUnit tOrganizationUnit = new TOrganizationUnit();
        tOrganizationUnit.setId(organizationUnit.getId());
        tOrganizationUnit.setDisplayName(organizationUnit.getDisplayName());
        return tOrganizationUnit;
    }

    public static TProgram from(Program program) {
        TProgram tProgram = new TProgram();
        tProgram.setId(program.getId());
        tProgram.setDisplayName(program.getDisplayName());
        return tProgram;
    }

}
