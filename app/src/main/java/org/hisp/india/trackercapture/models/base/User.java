package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class User extends BaseModel implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("surname")
    private String surName;
    @SerializedName("email")
    private String email;
    @SerializedName("organisationUnits")
    private List<OrganizationUnit> organizationUnits;

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public String getEmail() {
        return email;
    }

    public List<OrganizationUnit> getOrganizationUnits() {
        return organizationUnits;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
               "name='" + name + '\'' +
               ", firstName='" + firstName + '\'' +
               ", surName='" + surName + '\'' +
               ", email='" + email + '\'' +
               ", organisationUnits=" + organizationUnits +
               '}';
    }
}
