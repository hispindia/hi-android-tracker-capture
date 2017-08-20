package org.hisp.india.trackercapture.models.tmp;

import org.hisp.india.trackercapture.models.storage.RProgramTrackedEntityAttribute;

/**
 * Created by nhancao on 6/27/17.
 */

public class TMItem {
    public static final int INCIDENT_DATE = 0;
    public static final int ENROLLMENT_DATE = 1;
    public static final int FIELD_LIST = 2;
    public static final int REGISTER_BUTTON = 3;

    private RProgramTrackedEntityAttribute programTrackedEntityAttribute;
    private TMHeaderDate headerDateModel;
    private int type;

    private TMItem(int type, TMHeaderDate headerDateModel,
                   RProgramTrackedEntityAttribute programTrackedEntityAttribute) {
        this.type = type;
        this.headerDateModel = headerDateModel;
        this.programTrackedEntityAttribute = programTrackedEntityAttribute;
    }

    public static TMItem createIncidentDate(String label) {
        return new TMItem(INCIDENT_DATE, new TMHeaderDate(label), null);
    }

    public static TMItem createIncidentDate(String label, boolean isAllowFutureDate) {
        return new TMItem(INCIDENT_DATE, new TMHeaderDate(label, isAllowFutureDate), null);
    }

    public static TMItem createEnrollmentDate(String label) {
        return new TMItem(ENROLLMENT_DATE, new TMHeaderDate(label), null);
    }

    public static TMItem createEnrollmentDate(String label, boolean isAllowFutureDate) {
        return new TMItem(ENROLLMENT_DATE, new TMHeaderDate(label, isAllowFutureDate), null);
    }

    public static TMItem createRegisterFieldItem(RProgramTrackedEntityAttribute programTrackedEntityAttribute) {
        return new TMItem(FIELD_LIST, null, programTrackedEntityAttribute);
    }

    public static TMItem createRegisterButton() {
        return new TMItem(REGISTER_BUTTON, null, null);
    }

    public RProgramTrackedEntityAttribute getProgramTrackedEntityAttribute() {
        return programTrackedEntityAttribute;
    }

    public TMHeaderDate getHeaderDateModel() {
        return headerDateModel;
    }

    public int getType() {
        return type;
    }

}