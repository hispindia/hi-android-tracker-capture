package org.hisp.india.trackercapture.models.tmp;

/**
 * Created by nhancao on 6/27/17.
 */

public class HeaderDateModel {
    private String label;
    private boolean isAllowFutureDate;
    private String value;

    public HeaderDateModel(String label) {
        this.label = label;
    }

    public HeaderDateModel(String label, boolean isAllowFutureDate) {
        this.label = label;
        this.isAllowFutureDate = isAllowFutureDate;
    }

    public String getLabel() {
        return label;
    }

    public boolean isAllowFutureDate() {
        return isAllowFutureDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}