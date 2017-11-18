/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.sml.iwi.gpi.musterloesung.eks.endpoint;

import ch.ech.xmlns.ech_0194._1.InfoType;
import ch.ech.xmlns.ech_0194._1.PlatformMoveData;
import ch.zhaw.sml.iwi.gpi.musterloesung.eks.controller.EinwohnerKontrollSystemController;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author scep
 */
@WebService(name="Einwohnerkontrollsystem-Service", portName="EinwohnerKontrollSystemServicePort", targetNamespace = "http://www.ech.ch/xmlns/eCH-0194/1")
@SchemaValidation
public class EinwohnerKontrollSystemService {
    
    @Autowired
    private EinwohnerKontrollSystemController einwohnerKontrollSystemController;
    
    @WebMethod(operationName = "handlePlatformMoveData")
    @WebResult(name="infoType")
    public InfoType handlePlatformMoveData(@WebParam(name="platformMoveData") PlatformMoveData platformMoveData){
        return einwohnerKontrollSystemController.umzugsMeldungVerarbeiten(platformMoveData);
    }
}
