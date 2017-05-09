package org.hisp.india.trackercapture.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nhancao on 5/9/17.
 */

public class AttributeRequest {
    @SerializedName("attribute")
    private String attributeId;
    @SerializedName("value")
    private String value;

}
