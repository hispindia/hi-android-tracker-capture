package org.hisp.india.trackercapture.services.account;

import org.hisp.india.trackercapture.models.base.User;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nhancao on 4/9/17.
 */

public interface AccountApi {


    @GET("api/me?fields=id,created,lastUpdated,name,displayName," +
                 "firstName,surname,gender,birthday,introduction,education,employer,interests,jobTitle," +
                 "languages,email,phoneNumber,organisationUnits[code,level,id,displayName,parent,programs" +
                 "[id,displayName,trackedEntity,withoutRegistration,programRuleVariables[*],programStages" +
                 "[*,programStageSections[*,programStageDataElements[*,dataElement[*]]],programStageDataElements" +
                 "[*,dataElement[*,optionSet[id,displayName,valueType,options[id,displayName,code]]]]]," +
                 "programRules[*,programRuleActions[id,programRuleActionType]],enrollmentDateLabel" +
                 ",selectEnrollmentDatesInFuture,incidentDateLabel,selectIncidentDatesInFuture," +
                 "displayIncidentDate,programTrackedEntityAttributes[*,trackedEntityAttribute" +
                 "[id,displayName,optionSetValue,optionSet[id,displayName,valueType,options[id,displayName,code]]]]]]")
    Observable<User> login();
}
