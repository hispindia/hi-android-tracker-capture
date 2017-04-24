package org.hisp.india.trackercapture.services.organization;

import org.hisp.india.trackercapture.models.storage.TOrganizationUnit;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nhancao on 4/24/17.
 */

public class MOrganization {

    public static List<TOrganizationUnit> getAllOrganization() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<TOrganizationUnit> tOrganizationUnits = realm.where(TOrganizationUnit.class).findAll();
            return realm.copyFromRealm(tOrganizationUnits);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

        return new ArrayList<>();
    }

    public static void insertOrUpdate(List<TOrganizationUnit> organizationUnitList) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.insertOrUpdate(organizationUnitList);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

}
