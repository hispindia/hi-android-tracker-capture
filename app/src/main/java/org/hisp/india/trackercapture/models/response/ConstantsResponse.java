package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.Constant;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class ConstantsResponse {

    @Expose
    @SerializedName("constants")
    private List<Constant> constants;

    public List<Constant> getConstants() {
        return constants;
    }
}
