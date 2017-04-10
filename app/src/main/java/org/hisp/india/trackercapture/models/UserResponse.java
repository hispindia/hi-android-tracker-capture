package org.hisp.india.trackercapture.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class UserResponse implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("surname")
    private String surName;
    @SerializedName("email")
    private String email;
    @SerializedName("organisationUnits")
    private List<OrganisationUnit> organisationUnits;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
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

    public List<OrganisationUnit> getOrganisationUnits() {
        return organisationUnits;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surName='" + surName + '\'' +
                ", email='" + email + '\'' +
                ", organisationUnits=" + organisationUnits +
                '}';
    }
}
