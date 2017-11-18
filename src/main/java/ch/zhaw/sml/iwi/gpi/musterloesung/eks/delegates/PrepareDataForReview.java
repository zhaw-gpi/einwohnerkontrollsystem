/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.sml.iwi.gpi.musterloesung.eks.delegates;

import java.util.Map;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 *
 * @author scep
 */
@Named("concatenateDataForReviewAdapter")
public class PrepareDataForReview implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Map<String,Object> variables = execution.getVariables();
        String meldungsTyp = (String) variables.get("Meldungstyp");
        String vorgang = "Bla";
        if(meldungsTyp.equals("platformMoveData")){
            vorgang = "Umzug";
        }
        
        String dataToReview = "Folgende Daten zum " + vorgang + " wurden gemeldet:\n\n"
                + "Personendaten:\n" + "Vorname: " + (String) variables.get("firstName");
    }
    
    
}
