package org.hisp.india.trackercapture.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nhancao on 4/16/17.
 */

public class BaseModel implements Serializable, Model {

    @SerializedName("id")
    private String id;
    @SerializedName("displayName")
    private String displayName;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
