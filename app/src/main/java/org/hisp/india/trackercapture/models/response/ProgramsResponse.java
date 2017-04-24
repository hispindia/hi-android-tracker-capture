package org.hisp.india.trackercapture.models.response;

import com.google.gson.annotations.SerializedName;

import org.hisp.india.trackercapture.models.base.Program;

import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class ProgramsResponse {

    @SerializedName("programs")
    private List<Program> programs;

    public List<Program> getPrograms() {
        return programs;
    }
}
