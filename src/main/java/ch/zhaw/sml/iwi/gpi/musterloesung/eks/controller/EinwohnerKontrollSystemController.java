/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.sml.iwi.gpi.musterloesung.eks.controller;

import ch.ech.xmlns.ech_0011._8.DwellingAddressType;
import ch.ech.xmlns.ech_0044._4.PersonIdentificationType;
import ch.ech.xmlns.ech_0185._1.ContactDataType;
import ch.ech.xmlns.ech_0194._1.InfoType;
import ch.ech.xmlns.ech_0194._1.PlatformMoveCommit;
import ch.ech.xmlns.ech_0194._1.PlatformMoveData;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author scep
 */
public class EinwohnerKontrollSystemController {
    
    @Autowired
    private ProcessEngine processEngine;
    
    public InfoType umzugsMeldungVerarbeiten(PlatformMoveData platformMoveData) {
        PersonIdentificationType personIdentification = platformMoveData.getPersonIdentification();
        DwellingAddressType dwellingAddress = platformMoveData.getDwellingAddress();
        
        Map<String,Object> variables = new HashMap<>();
        variables.put("Meldungstyp", "platformMoveData");
        variables.put("firstName", personIdentification.getFirstName());
        variables.put("officialName", personIdentification.getOfficialName());
        variables.put("sex", personIdentification.getSex());
        variables.put("dateOfBirth", personIdentification.getDateOfBirth().getYearMonthDay());
        variables.put("vn", personIdentification.getVn());
        
        ContactDataType contactData = platformMoveData.getContactData();
        variables.put("emailAddress", contactData.getEmailAddress());
        variables.put("phoneNumber", contactData.getPhoneNumber());
        
        variables.put("movingDate", dwellingAddress.getMovingDate());
        variables.put("street", dwellingAddress.getAddress().getStreet());
        variables.put("houseNumber", dwellingAddress.getAddress().getHouseNumber());
        variables.put("town", dwellingAddress.getAddress().getTown());
        variables.put("swissZipCode", dwellingAddress.getAddress().getSwissZipCode());
        
        variables.put("administrativDwellingNumber", platformMoveData.getDwellingIdentification().getAdministrativDwellingNumber());
        
        ProcessInstanceWithVariables processInstanceWithVariables = processEngine.
                getRuntimeService().createProcessInstanceByKey("RelocationMessageHandlingProcess").
                setVariables(variables).executeWithVariablesInReturn();
        
        Map<String,Object> processEndVariables = processInstanceWithVariables.getVariables();
        
        InfoType infoType = new InfoType();
        
        infoType.setCode((BigInteger) processEndVariables.get("code"));
        infoType.setTextGerman((String) processEndVariables.get("textGerman"));
        
        return infoType;
    }
}
