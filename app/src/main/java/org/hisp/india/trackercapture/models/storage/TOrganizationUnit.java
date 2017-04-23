package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/16/17.
 */

public class TOrganizationUnit extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String displayName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


}
