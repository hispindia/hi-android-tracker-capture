package org.hisp.india.trackercapture.models.base;

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

    public BaseModel() {
    }

    public BaseModel(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
