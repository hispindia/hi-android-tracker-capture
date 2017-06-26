package org.hisp.india.trackercapture.services.organization;

import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RUser;
import org.hisp.india.trackercapture.utils.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.RealmResults;

import static org.hisp.india.trackercapture.utils.RealmHelper.query;

/**
 * Created by nhancao on 4/24/17.
 */

public class OrganizationQuery {

    /**
     * Get all organization from local of current user
     */
    public static List<ROrganizationUnit> getUserOrganizations() {
        return query(realm -> {
            RUser user = realm.where(RUser.class).findFirst();
            if (user == null) return new ArrayList<ROrganizationUnit>();
            return realm.copyFromRealm(user.getOrganizationUnits());
        });
    }

    /**
     * Get all organization from local
     */
    public static List<ROrganizationUnit> getAllOrganization() {
        return query(realm -> {
            RealmResults<ROrganizationUnit> tOrganizationUnits = realm.where(ROrganizationUnit.class).findAll();
            return realm.copyFromRealm(tOrganizationUnits);
        });
    }

    /**
     * Insert or update org
     */
    public static void insertOrUpdate(List<ROrganizationUnit> organizationUnitList) {
        RealmHelper.transaction(realm -> realm.insertOrUpdate(organizationUnitList));
    }

    /**
     * Check data on local is exist or not
     */
    public static boolean isEmpty() {
        Long count = query(realm -> realm.where(ROrganizationUnit.class).count());
        return count == null || count <= 0;
    }

    /**
     * Get organization from local by parent id
     */
    public static List<ROrganizationUnit> getOrgFromLocalByParent(final String parent) {
        return query(realm -> {
            RealmResults<ROrganizationUnit> realmResults = realm.where(ROrganizationUnit.class)
                                                                .equalTo("parent", parent)
                                                                .findAll();
            return realm.copyFromRealm(realmResults);
        });
    }

    /**
     * Get all org from local by parentId and skip to lower level
     */
    public static List<ROrganizationUnit> getOrgFromLocalByLevel(final String parentId, final int lowerLevel) {

        final List<ROrganizationUnit> empty = new ArrayList<>();
        final List<String> parentIds = new ArrayList<>();

        final ROrganizationUnit parent = RealmHelper
                .query(realm -> realm.copyFromRealm(
                        realm.where(ROrganizationUnit.class).equalTo("id", parentId).findFirst()));
        if (parent == null) return empty;

        final int topLevel = parent.getLevel() + 1;
        for (int i = topLevel; i <= lowerLevel; i++) {
            final int finalI = i;
            List<ROrganizationUnit> listLevel = RealmHelper
                    .query(realm -> {
                        RealmResults<ROrganizationUnit> realmResults;
                        RealmQuery<ROrganizationUnit> query = realm.where(ROrganizationUnit.class)
                                                                   .equalTo("level", finalI);
                        if (finalI == topLevel) {
                            realmResults = query.equalTo("parent", parentId).findAll();
                        } else {
                            realmResults = query.in("parent", parentIds.toArray(new String[0]))
                                                .findAll();
                        }
                        return realm.copyFromRealm(realmResults);
                    });

            if (listLevel == null) {
                return empty;
            } else if (finalI == lowerLevel) {
                return listLevel;
            }

            parentIds.clear();
            for (ROrganizationUnit rOrganisationUnit : listLevel) {
                parentIds.add(rOrganisationUnit.getId());
            }

        }
        return empty;

    }

    /**
     * Get organization from local by level
     */
    public static List<ROrganizationUnit> getOrgFromLocalByLevel(final int level) {
        return query(realm -> {
            RealmResults<ROrganizationUnit> realmResults = realm.where(ROrganizationUnit.class)
                                                                .equalTo("level", level)
                                                                .findAll();
            return realm.copyFromRealm(realmResults);
        });
    }

    /**
     * Get organization from local by top level and lower level
     */
    public static List<ROrganizationUnit> getOrgFromLocalByLevel(final int topLevel, final int lowerLevel) {

        final List<ROrganizationUnit> empty = new ArrayList<>();
        final List<String> parentIds = new ArrayList<>();

        for (int i = topLevel; i <= lowerLevel; i++) {
            final int finalI = i;
            List<ROrganizationUnit> listLevel = RealmHelper
                    .query(realm -> {
                        RealmResults<ROrganizationUnit> realmResults;
                        RealmQuery<ROrganizationUnit> query = realm.where(ROrganizationUnit.class)
                                                                   .equalTo("level", finalI);
                        if (finalI == topLevel) {
                            realmResults = query.findAll();
                        } else {
                            realmResults = query.in("parent", parentIds.toArray(new String[0]))
                                                .findAll();
                        }
                        return realm.copyFromRealm(realmResults);
                    });

            if (listLevel == null) {
                return empty;
            } else if (finalI == lowerLevel) {
                return listLevel;
            }

            parentIds.clear();
            for (ROrganizationUnit rOrganisationUnit : listLevel) {
                parentIds.add(rOrganisationUnit.getId());
            }

        }
        return empty;

    }

    /**
     * Get organization from local by id
     */
    public static List<ROrganizationUnit> getOrganisationUnitId(final String id) {
        return query(realm -> {
            RealmResults<ROrganizationUnit> realmResults = realm.where(ROrganizationUnit.class)
                                                                .equalTo("displayName", id)
                                                                .findAll();
            return realm.copyFromRealm(realmResults);
        });
    }

}
