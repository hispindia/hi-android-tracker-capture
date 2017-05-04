package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class OrganizationUnit extends BaseModel implements Serializable {

    @SerializedName("programs")
    private List<Program> programs;

    public List<Program> getPrograms() {
        return programs;
    }
}
