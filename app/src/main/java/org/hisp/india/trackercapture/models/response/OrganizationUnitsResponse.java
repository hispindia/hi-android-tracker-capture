package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.OrganizationUnit;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class OrganizationUnitsResponse {

    @Expose
    @SerializedName("pager")
    private PageResponse pageResponse;

    @Expose
    @SerializedName("organisationUnits")
    private List<OrganizationUnit> organizationUnits;

    public List<OrganizationUnit> getOrganizationUnits() {
        return organizationUnits;
    }

    public PageResponse getPageResponse() {
        return pageResponse;
    }
}
