package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/16/17.
 */

public class ROrganizationUnit extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String displayName;
    private RealmList<RProgram> programs;

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

    public RealmList<RProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(RealmList<RProgram> programs) {
        this.programs = programs;
    }
}
