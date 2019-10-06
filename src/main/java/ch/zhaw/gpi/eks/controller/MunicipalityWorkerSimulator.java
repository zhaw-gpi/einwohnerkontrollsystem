package ch.zhaw.gpi.eks.controller;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ch.zhaw.gpi.eks.resources.PlatformMove;
import ch.zhaw.gpi.eks.resources.TechnicalReceipt;

/**
 * Simuliert einen menschlichen Mitarbeiter bei der Einwohnerkontrolle
 */
@Component
public class MunicipalityWorkerSimulator {

    @Async
    public void checkDataAndSendResponse(PlatformMove platformMove) throws InterruptedException {
        // Zufällige Zeit warten zwischen 3 und 30 Sekunden
        Random random = new Random();
        TimeUnit.SECONDS.sleep(random.nextInt(28) + 3);

        // Response-Code "berechnen" auf Basis des ersten Buchstabens des Vornamens (a-m => Annahme [1], n-z => Ablehnung [0])
        char firstLetter = platformMove.getFirstName().charAt(0);
        Integer responseCode = ((firstLetter > 'm' && firstLetter < 'z') || (firstLetter > 'M' && firstLetter < 'Z')) ? 0 : 1;

        // Header hinzufügen (JSON-Nachricht)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        // Antwort aufbereiten
        TechnicalReceipt technicalReceipt = new TechnicalReceipt();
        technicalReceipt.setBusinessKey(platformMove.getBusinessKey());
        technicalReceipt.setResponseCode(responseCode);
        
        HttpEntity<TechnicalReceipt> httpEntity = new HttpEntity<TechnicalReceipt>(technicalReceipt, headers);

        ResponseEntity<Object> responseEntity = new RestTemplate().postForEntity(platformMove.getCallbackUrl(), httpEntity, null);

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException(responseEntity.getStatusCode().toString() + ": " + responseEntity.getBody());
        }
        
        System.out.println("Antwort aufbereitet und versendet: " + technicalReceipt.getResponseCode().toString());
    }
}