package org.hisp.india.trackercapture.models.storage;

import org.hisp.india.trackercapture.models.base.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nhancao on 4/24/17.
 */

public class RProgramRule extends RealmObject implements Model {

    @PrimaryKey
    private String id;
    private String displayName;
    private String condition;
    private String description;
    private RealmList<RProgramRuleAction> programRuleActions;

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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RealmList<RProgramRuleAction> getProgramRuleActions() {
        return programRuleActions;
    }

    public void setProgramRuleActions(
            RealmList<RProgramRuleAction> programRuleActions) {
        this.programRuleActions = programRuleActions;
    }
}
