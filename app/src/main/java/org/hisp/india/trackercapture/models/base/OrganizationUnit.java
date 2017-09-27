package org.hisp.india.trackercapture.models.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhancao on 4/9/17.
 */

public class OrganizationUnit extends BaseModel implements Serializable {

    @Expose
    @SerializedName("programs")
    private List<Program> programs;
    @Expose
    @SerializedName("level")
    private int level;
    @Expose
    @SerializedName("code")
    private String code;

    @Expose
    @SerializedName("parent")
    private BaseModel parent;

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public int getLevel() {
        return level;
    }

    public BaseModel getParent() {
        return parent;
    }

    public String getCode() {
        return code;
    }
}
