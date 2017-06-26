package org.hisp.india.trackercapture.services.organization;

import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class OrganizationQuery {

    public static List<ROrganizationUnit> getUserOrganizations() {
        return RealmHelper.query(realm -> {
            RUser user = realm.where(RUser.class).findFirst();
            if (user == null) return new ArrayList<ROrganizationUnit>();
            return realm.copyFromRealm(user.getOrganizationUnits());
        });
    }

    public static List<ROrganizationUnit> getAllOrganization() {
        return RealmHelper.query(realm -> {
            RealmResults<ROrganizationUnit> tOrganizationUnits = realm.where(ROrganizationUnit.class).findAll();
            return realm.copyFromRealm(tOrganizationUnits);
        });
    }

    public static void insertOrUpdate(List<ROrganizationUnit> organizationUnitList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(organizationUnitList));
    }

}
