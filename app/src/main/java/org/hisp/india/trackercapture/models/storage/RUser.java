package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 5/4/17.
 */

public class RUser extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String displayName;
    private String email;
    private String name;
    private String firstName;
    private String surName;
    private RealmList<ROrganizationUnit> organizationUnits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RealmList<ROrganizationUnit> getOrganizationUnits() {
        return organizationUnits;
    }

    public void setOrganizationUnits(
            RealmList<ROrganizationUnit> organizationUnits) {
        this.organizationUnits = organizationUnits;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
