package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class User extends BaseModel implements Serializable {

    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("firstName")
    private String firstName;
    @Expose
    @SerializedName("surname")
    private String surName;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
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
