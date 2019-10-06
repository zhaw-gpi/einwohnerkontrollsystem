package ch.zhaw.gpi.eks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.gpi.eks.resources.PlatformMove;

/**
 * REST-Controller
 * 
 * @author scep
 */
@RestController
public class EksRestController {

    @Autowired
    private MunicipalityWorkerSimulator municipalityWorkerSimulator;
    
    /**
     * REST-Ressource für URL /eksapi/v1/platformmove/ (POST)
     * 
     * @return HTTP-Response mit einem Status 200
     * @throws InterruptedException
     */
    @PostMapping(value = "/eksapi/v1/platformmove/")
    public ResponseEntity<HttpStatus> addPlatformMove(@RequestBody PlatformMove platformMove) throws InterruptedException {
        // Eine andere Methode asynchron aufrufen, welche später dem aufrufenden System antwortet
        municipalityWorkerSimulator.checkDataAndSendResponse(platformMove);
        
        // Positiven Status zurückgeben
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);   
    }
}
