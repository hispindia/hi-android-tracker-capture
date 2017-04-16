package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.OrganizationUnit;

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

}
