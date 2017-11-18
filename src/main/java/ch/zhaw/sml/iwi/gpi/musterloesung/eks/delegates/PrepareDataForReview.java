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
        
        String nachrichtenTyp = (String) variables.get("nachrichtenTyp");
        
        String dataToReview = "<p>Bitte prüfen Sie, ob der folgende Antrag zum "
                + nachrichtenTyp + " bewilligt werden kann.</p>";
    }    
}
