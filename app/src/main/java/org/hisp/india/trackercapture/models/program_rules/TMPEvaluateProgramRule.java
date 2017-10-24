package org.hisp.india.trackercapture.models.program_rules;

import org.hisp.india.trackercapture.models.storage.RProgramStageDataElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Ahmed on 10/24/2017.
 */

public class TMPEvaluateProgramRule {
    public static List<String> fieldsToHide(List<RProgramStageDataElement> completeListOfDataElements) {//, String programId){
        //this should be later changed to act dynamically

        List<String> dataElementsToHide = new ArrayList<>();
        String program = "jC8Gprj4pWV";//hardcoding for program NPCDCS

        for(RProgramStageDataElement dataElement:completeListOfDataElements){
            if (dataElement!=null && dataElement.getValueDisplay()!=null &&dataElement.getValueDisplay().equalsIgnoreCase("NO")) {
                dataElementsToHide.add("asJUbKC02eS");//CA
                dataElementsToHide.add("a538diiIJyC");//CKD
                dataElementsToHide.add("G0sHYJ2nD7e");//CVD
                dataElementsToHide.add("ucwbkYdEqRB");//DM
                dataElementsToHide.add("jHssYs1oIHE");//HTN
                dataElementsToHide.add("XriWAaJ74CD");//RF
            }
        }

        return dataElementsToHide;
    }
}
