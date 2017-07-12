package org.hisp.india.trackercapture.models.e_num;

/**
 * Created by nhancao on 7/13/17.
 */

public enum OrgValueType {
    State("State", 3),
    District("District", 5),
    Block("Block / Taluk", 6),
    Village("Village", 7);

    private String token;
    private int level;
    private String parent;

    OrgValueType(String token, int level) {
        this.token = token;
        this.level = level;
    }

    public static OrgValueType getType(String fieldName) {
        try {
            for (OrgValueType orgValueType : OrgValueType.values()) {
                if (fieldName.contains(orgValueType.getToken())) {
                    return orgValueType;
                }
            }
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getToken() {
        return token;
    }

    public int getLevel() {
        return level;
    }
}
