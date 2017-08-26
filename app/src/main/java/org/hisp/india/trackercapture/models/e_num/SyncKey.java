package org.hisp.india.trackercapture.models.e_num;

/**
 * Created by nhancao on 7/11/17.
 */

public enum SyncKey {
    ROrganizationUnit,
    RTaskTrackedEntityInstance;


    public static SyncKey getType(String syncKey) {
        try {
            return SyncKey.valueOf(syncKey);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }
}
