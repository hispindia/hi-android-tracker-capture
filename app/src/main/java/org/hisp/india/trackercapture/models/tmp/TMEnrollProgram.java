package org.hisp.india.trackercapture.models.tmp;

import com.google.gson.Gson;

import org.hisp.india.trackercapture.models.storage.RProgram;
import org.hisp.india.trackercapture.models.storage.RTaskAttribute;
import org.hisp.india.trackercapture.models.storage.RTaskEnrollment;
import org.hisp.india.trackercapture.models.storage.RTaskRequest;
import org.hisp.india.trackercapture.models.storage.RTaskTrackedEntityInstance;
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
    public TMEnrollProgram(RTaskRequest taskRequest) {
        RTaskEnrollment enrollment = taskRequest.getEnrollment();
        if (enrollment != null) {
            this.organizationUnitId = enrollment.getOrgUnitId();
            this.programId = enrollment.getProgramId();
        }
        this.taskRequest = taskRequest;
    }

    public TMEnrollProgram(String organizationUnitId, String programId) {
        this.organizationUnitId = organizationUnitId;
        this.programId = programId;
        this.taskRequest = new RTaskRequest();
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

    public TMEnrollProgram setEnrollment(String enrollmentDateValue, String incidentDateValue) {
        RTaskEnrollment taskEnrollment = taskRequest.getEnrollment();
        if (taskEnrollment != null) {
            taskEnrollment.setProgramId(programId);
            taskEnrollment.setOrgUnitId(organizationUnitId);
            taskEnrollment.setEnrollmentDate(enrollmentDateValue);
            taskEnrollment.setIncidentDate(incidentDateValue);
        } else {
            taskRequest.setEnrollment(RTaskEnrollment.create(programId,
                                                             organizationUnitId,
                                                             enrollmentDateValue,
                                                             incidentDateValue));
        }
        return this;
    }

    public RProgram getProgram() {
        return ProgramQuery.getProgram(programId);
    }

    public TMEnrollProgram setTrackedEntityInstance(List<RTaskAttribute> taskAttributeList) {
        RProgram program = getProgram();
        if (program != null) {
            RTaskTrackedEntityInstance trackedEntityInstance = taskRequest.getTrackedEntityInstance();

            if (trackedEntityInstance != null) {
                trackedEntityInstance.setTrackedEntityId(program.getTrackedEntity().getId());
                trackedEntityInstance.setOrgUnitId(organizationUnitId);
                trackedEntityInstance.setAttributeRequestList(taskAttributeList);
            } else {
                taskRequest
                        .setTrackedEntityInstance(RTaskTrackedEntityInstance.create(program.getTrackedEntity().getId(),
                                                                                    organizationUnitId,
                                                                                    taskAttributeList
                                                                                   ));
            }

        }
        return this;
    }
}
