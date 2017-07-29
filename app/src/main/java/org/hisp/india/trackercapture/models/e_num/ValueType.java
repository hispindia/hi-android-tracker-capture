package org.hisp.india.trackercapture.models.e_num;

/**
 * Created by nhancao on 5/7/17.
 */

public enum ValueType {
    TEXT,
    LONG_TEXT,
    LETTER,
    PHONE_NUMBER,
    EMAIL,
    BOOLEAN,
    DATE,
    DATE_TIME,
    TIME,
    NUMBER,
    ORGANISATION_UNIT,
    TRACKER_ASSOCIATE,
    OTHER;

    public static ValueType getType(String valueType) {
        try {
            return ValueType.valueOf(valueType);
        } catch (IllegalArgumentException ignored) {
        }
        return ValueType.OTHER;
    }
}
