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
    @SerializedName("level")
    private int level;
    @SerializedName("code")
    private String code;

    @SerializedName("parent")
    private String parent;

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public int getLevel() {
        return level;
    }

    public String getParent() {
        return parent;
    }

    public String getCode() {
        return code;
    }
}
