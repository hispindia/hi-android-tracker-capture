package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 5/10/17.
 */

public class HeaderResponse implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("column")
    private String column;
    @SerializedName("type")
    private boolean hidden;
    @SerializedName("meta")
    private boolean meta;

    public String getName() {
        return name;
    }

    public String getColumn() {
        return column;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isMeta() {
        return meta;
    }

    @Override
    public String toString() {
        return "HeaderResponse{" +
               "name='" + name + '\'' +
               ", column='" + column + '\'' +
               ", hidden=" + hidden +
               ", meta=" + meta +
               '}';
    }
}
