package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/24/17.
 */

public class ROption extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String code;
    private String displayName;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
