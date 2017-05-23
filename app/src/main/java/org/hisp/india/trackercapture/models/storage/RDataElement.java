package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/24/17.
 */

public class RDataElement extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String displayName;
    private String valueType;
    private boolean optionSetValue;
    private ROptionSet optionSet;

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

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public boolean isOptionSetValue() {
        return optionSetValue;
    }

    public void setOptionSetValue(boolean optionSetValue) {
        this.optionSetValue = optionSetValue;
    }

    public ROptionSet getOptionSet() {
        return optionSet;
    }

    public void setOptionSet(ROptionSet optionSet) {
        this.optionSet = optionSet;
    }
}
