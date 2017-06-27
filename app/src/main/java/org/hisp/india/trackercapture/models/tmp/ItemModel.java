package org.hisp.india.trackercapture.models.tmp;

import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;

/**
 * Created by nhancao on 6/27/17.
 */

public class ItemModel {
    public static final int INCIDENT_DATE = 0;
    public static final int ENROLLMENT_DATE = 1;
    public static final int FIELD_LIST = 2;
    public static final int REGISTER_BUTTON = 3;

    private RProgramTrackedEntityAttribute programTrackedEntityAttribute;
    private HeaderDateModel headerDateModel;
    private int type;

    private ItemModel(int type, HeaderDateModel headerDateModel,
                      RProgramTrackedEntityAttribute programTrackedEntityAttribute) {
        this.type = type;
        this.headerDateModel = headerDateModel;
        this.programTrackedEntityAttribute = programTrackedEntityAttribute;
    }

    public static ItemModel createIncidentDate(String label) {
        return new ItemModel(INCIDENT_DATE, new HeaderDateModel(label), null);
    }

    public static ItemModel createIncidentDate(String label, boolean isAllowFutureDate) {
        return new ItemModel(INCIDENT_DATE, new HeaderDateModel(label, isAllowFutureDate), null);
    }

    public static ItemModel createEnrollmentDate(String label) {
        return new ItemModel(ENROLLMENT_DATE, new HeaderDateModel(label), null);
    }

    public static ItemModel createEnrollmentDate(String label, boolean isAllowFutureDate) {
        return new ItemModel(ENROLLMENT_DATE, new HeaderDateModel(label, isAllowFutureDate), null);
    }

    public static ItemModel createRegisterFieldItem(RProgramTrackedEntityAttribute programTrackedEntityAttribute) {
        return new ItemModel(FIELD_LIST, null, programTrackedEntityAttribute);
    }

    public static ItemModel createRegisterButton() {
        return new ItemModel(REGISTER_BUTTON, null, null);
    }

    public RProgramTrackedEntityAttribute getProgramTrackedEntityAttribute() {
        return programTrackedEntityAttribute;
    }

    public HeaderDateModel getHeaderDateModel() {
        return headerDateModel;
    }

    public int getType() {
        return type;
    }

}