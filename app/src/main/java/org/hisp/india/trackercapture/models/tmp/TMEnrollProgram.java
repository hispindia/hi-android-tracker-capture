package org.hisp.india.trackercapture.models.tmp;

import com.google.gson.Gson;

import org.hisp.india.trackercapture.models.storage.ROrganizationUnit;
import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RTaskAttribute;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.storage.RTaskTrackedEntityInstance;
import org.hisp.india.trackercapture.services.organization.OrganizationQuery;
import org.hisp.india.trackercapture.services.programs.ProgramQuery;

import java.util.List;

/**
 * This class included all resources which will render on ui for Enroll Program
 * <p>
 * Created by nhancao on 8/20/17.
 */
public class TMEnrollProgram {

    private String programId;
    private String organizationUnitId;
    private RTaskRequest taskRequest;

    /////////////////////////
    private RProgram program;
    private ROrganizationUnit organizationUnit;

    public TMEnrollProgram(ROrganizationUnit organizationUnit, RProgram program) {
        this.organizationUnit = organizationUnit;
        this.program = program;
        this.taskRequest = new RTaskRequest();

        //////////////////////
        this.organizationUnitId = organizationUnit.getId();
        this.programId = program.getId();
    }

    public static String toJson(TMEnrollProgram tmEnrollProgram) {
        return new Gson().toJson(tmEnrollProgram);
    }

    public static TMEnrollProgram fromJson(String json) {
        return new Gson().fromJson(json, TMEnrollProgram.class);
    }

    public String getOrganizationUnitId() {
        return organizationUnitId;
    }

    public void setOrganizationUnitId(String organizationUnitId) {
        this.organizationUnitId = organizationUnitId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public RTaskRequest getTaskRequest() {
        return taskRequest;
    }

    public void setTaskRequest(RTaskRequest taskRequest) {
        this.taskRequest = taskRequest;
    }

    public RProgram getProgram() {
        return program;
    }

    public void reloadCache() {
        this.program = ProgramQuery.getProgram(programId);
        this.organizationUnit = OrganizationQuery.getOrganisationUnitId(organizationUnitId);
    }

    public TMEnrollProgram setEnrollment(String enrollmentDateValue, String incidentDateValue) {
        this.taskRequest.setEnrollment(RTaskEnrollment.create(programId,
                                                              organizationUnitId,
                                                              enrollmentDateValue,
                                                              incidentDateValue));
        return this;
    }

    public TMEnrollProgram setTrackedEntityInstance(List<RTaskAttribute> taskAttributeList) {
        this.taskRequest.setTrackedEntityInstance(RTaskTrackedEntityInstance.create(program.getTrackedEntity().getId(),
                                                                                    organizationUnitId,
                                                                                    taskAttributeList
                                                                                   ));

        return this;
    }
}
