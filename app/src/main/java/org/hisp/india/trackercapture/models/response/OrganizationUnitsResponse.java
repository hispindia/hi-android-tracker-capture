package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.OrganizationUnit;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class OrganizationUnitsResponse {

    @SerializedName("organisationUnits")
    private List<OrganizationUnit> organizationUnits;

    public List<OrganizationUnit> getOrganizationUnits() {
        return organizationUnits;
    }
}
