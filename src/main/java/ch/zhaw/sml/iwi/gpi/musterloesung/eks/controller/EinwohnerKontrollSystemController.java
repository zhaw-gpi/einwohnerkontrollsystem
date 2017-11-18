package ch.zhaw.sml.iwi.gpi.musterloesung.eks.controller;

import ch.ech.xmlns.ech_0010._5.SwissAddressInformationType;
import ch.ech.xmlns.ech_0011._8.DwellingAddressType;
import ch.ech.xmlns.ech_0044._4.PersonIdentificationType;
import ch.ech.xmlns.ech_0058._5.HeaderType;
import ch.ech.xmlns.ech_0185._1.ContactDataType;
import ch.ech.xmlns.ech_0185._1.DwellingIdentificationType;
import ch.ech.xmlns.ech_0194._1.DeliveryType;
import ch.ech.xmlns.ech_0194._1.InfoType;
import ch.ech.xmlns.ech_0194._1.NegativeReportType;
import ch.ech.xmlns.ech_0194._1.PositiveReportType;
import ch.zhaw.sml.iwi.gpi.musterloesung.eks.helpers.DefaultHeaderHelper;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation für den EinwohnerKontrollSystemService Diese Klasse enthält
 * die eigentliche Implementation der Web Service-Operationen, wobei jede
 * Funktion einer gleich lautenden Web Service-Operation entspricht. Sie stellt
 * die Verbindung zwischen Web Service-Schnittstelle und Process Engine her
 */
public class EinwohnerKontrollSystemController {

    /**
     * Unser Web Service nutzt die Camunda Process Engine als Abhängigkeit Über
     *
     * @Autowired wird die zur Laufzeit verfügbare Process Engine-Instanz in
     * diesem Service als Variable processEngine eingefügt (Dependency
     * Injection)
     */
    @Autowired
    private ProcessEngine processEngine;

    /**
     * Implementation der Web Service-Operation handleDelivery Prüft, ob
     * wirklich eine der platformMove-Nachrichten übergeben wird. Falls nein,
     * wird ein negativeReport zurückgegeben. Falls ja, werden aus dem
     * erhaltenen DeliveryType-Objekt die relevanten Eigenschaften in
     * Prozessvariablen übersetzt. Daraufhin startet eine neue Instanz des
     * Prozesses RelocationMessageHandlingProcess mit diesen Prozessvariablen.
     * Die Variablen am Ende des Prozesses werden ausgelesen und den
     * entsprechenden Eigenschaften des positiveReport- oder
     * NegativeReport-Objekts zugewiesen. Dieses wird schliesslich gewrapped in
     * einem DeliveryType-Objekt an den Webservice-Endpoint zurück gegeben.
     *
     * @param deliveryRequest
     * @return
     */
    public DeliveryType handleDelivery(DeliveryType deliveryRequest) {
        // Header des Anfrage-Objekts in neue Variable auslesen
        HeaderType headerRequest = deliveryRequest.getDeliveryHeader();

        // Antwort-Objekt initialisieren
        DeliveryType deliveryResponse = new DeliveryType();

        // Header des Antwort-Objekts mit Default-Werten basierend auf Hilfsklasse generieren
        HeaderType headerResponse = new DefaultHeaderHelper(headerRequest).getHeaderResponse();

        // Eine String-List erstellen mit erlaubten Nachrichtentypen
        String[] allowedMessageTypes = {"sedex://platformMoveOut", "sedex://platformMoveIn", "sedex://platformMoveData"};

        // Tatsächlichen Nachrichtentyp in neue Variable auslesen
        String messageType = headerRequest.getMessageType();

        // Nur wenn der Nachrichten-Typ der Anfrage einem erlaubten Nachrichtentyp entspricht
        // soll es weiter gehen, ansonsten soll ein negativeReport zurück gesendet werden
        if (ArrayUtils.contains(allowedMessageTypes, messageType)) {
            // Map vorbereiten für die Übergabe an die Prozessinstanz als Prozessvariablen
            Map<String, Object> processStartVariables = new HashMap<>();

            // PersonIdentification, ContactData, Zuzugsadresse und WohnungsInformation des Anfrage-Objekts in neue Variable auslesen in Abhängigkeit des Nachrichtentyps
            PersonIdentificationType personIdentification = null;
            ContactDataType contactData = null;
            DwellingAddressType dwellingAddress = null;
            DwellingIdentificationType dwellingIdentification = null;
            String nachrichtenTyp = "";
            switch (messageType) {
                case "sedex://platformMoveOut":
                    nachrichtenTyp = "Wegzug";
                    personIdentification = deliveryRequest.getPlatformMoveOut().getPersonIdentification();
                    contactData = deliveryRequest.getPlatformMoveOut().getContactData();
                    dwellingAddress = deliveryRequest.getPlatformMoveOut().getDestination().getDestinationAddress();
                    break;
                case "sedex://platformMoveIn":
                    nachrichtenTyp = "Zuzug";
                    personIdentification = deliveryRequest.getPlatformMoveIn().getPersonIdentification();
                    contactData = deliveryRequest.getPlatformMoveIn().getContactData();
                    dwellingIdentification = deliveryRequest.getPlatformMoveIn().getDwellingIdentification();
                    break;
                case "sedex://platformMoveData":
                    nachrichtenTyp = "Umzug";
                    personIdentification = deliveryRequest.getPlatformMoveData().getPersonIdentification();
                    contactData = deliveryRequest.getPlatformMoveData().getContactData();
                    dwellingAddress = deliveryRequest.getPlatformMoveData().getDwellingAddress();
                    dwellingIdentification = deliveryRequest.getPlatformMoveData().getDwellingIdentification();
                    break;
            }
            
            // Nachrichtentyp an Prozessvariable übergeben
            processStartVariables.put("nachtrichenTyp", nachrichtenTyp);

            // AHV-Nummer aus PersonIdentification an Prozessvariable übergeben
            processStartVariables.put("vn", personIdentification.getVn());

            // Vorname aus PersonIdentification an Prozessvariable übergeben
            processStartVariables.put("firstName", personIdentification.getFirstName());

            // Nachname aus PersonIdentification an Prozessvariable übergeben
            processStartVariables.put("officialName", personIdentification.getOfficialName());

            // Geschlecht aus PersonIdentification an Prozessvariable übergeben
            processStartVariables.put("sex", Integer.parseInt(personIdentification.getSex()));

            // Geburtsdatum aus PersonIdentification in ein Date-Objekt umwandeln
            Date dateOfBirth = personIdentification.getDateOfBirth().getYearMonthDay().toGregorianCalendar().getTime();

            // Umgewandeltes Geburtsdatum aus PersonIdentification an Prozessvariable übergeben
            processStartVariables.put("dateOfBirth", dateOfBirth);

            // Email-Adresse aus ContactData an Prozessvariable übergeben
            processStartVariables.put("emailAddress", contactData.getEmailAddress());

            // Telefonnummer aus ContactData an Prozessvariable übergeben
            processStartVariables.put("phoneNumber", contactData.getPhoneNumber());

            // Adressangaben und Zuzugsdatum aus DwellingAdress an Prozessvariablen übergeben, sofern  nicht null
            if (dwellingAddress != null) {
                // SwissAddressInformation-Objekt aus DwellingAdress zuweisen
                SwissAddressInformationType swissAddressInformation = dwellingAddress.getAddress();

                // Strasse an Prozessvariable übergeben
                processStartVariables.put("street", swissAddressInformation.getStreet());

                // Hausnummer an Prozessvariable übergeben
                processStartVariables.put("houseNumber", swissAddressInformation.getHouseNumber());

                // PLZ an Prozessvariable übergeben
                processStartVariables.put("swissZipCode", swissAddressInformation.getSwissZipCode());

                // Ortschaft an Prozessvariable übergeben
                processStartVariables.put("town", swissAddressInformation.getTown());

                // Zuzugs-/Umzugsdatum an Prozessvariable übergeben
                processStartVariables.put("movingDate", dwellingAddress.getMovingDate());
            }

            // Wohnungsidentifikation an Prozessvariable übergeben, falls nicht null
            if (dwellingIdentification != null) {
                processStartVariables.put("administrativDwellingNumber", dwellingIdentification.getAdministrativDwellingNumber());
            }

            // Wegzugs-Meldung-spezifische Eigenschaften
            if (messageType.equals("sedex://platformMoveOut")) {
                // Wegzugsdatum an Prozessvariable übergeben
                processStartVariables.put("departureDate", deliveryRequest.getPlatformMoveOut().getDestination().getDepartureDate());
            }

            // Zuzugs-Meldung-spezifische Eigenschaften
            if (messageType.equals("sedex://platformMoveIn")) {
                // Zuzugsdatum an Prozessvariable übergeben
                processStartVariables.put("arrivalDate", deliveryRequest.getPlatformMoveIn().getArrivalDate());
            }

            // Eine neue Instanz des Prozesses mit der Id "RelocationMessageHandlingProcess" wird gestartet
            // Die vorbereiteten Variablen werden übergeben
            // Als Antwort kommen die zuletzt bekannten Prozessvariablen zurück
            // Da der Prozess hierbei vollständig durchgelaufen ist, kommen also die
            // Variablen zurück, wie sie beim Endereignis eingetroffen sind
            ProcessInstanceWithVariables processInstanceWithVariables = processEngine.getRuntimeService()
                    .createProcessInstanceByKey("RelocationMessageHandlingProcess")
                    .setVariables(processStartVariables)
                    .executeWithVariablesInReturn();

            // Prozess-Variablen am Prozessende an ein Map-Objekt übergeben
            Map<String, Object> processEndVariables = processInstanceWithVariables.getVariables();

            // Code-Prozessvariable wird einer neuen Code-Variable zugewiesen
            Integer codeInteger = (Integer) processEndVariables.get("code");
            BigInteger code = BigInteger.valueOf(codeInteger.longValue());

            // Ein neues InfoType-Objekt wird initialisiert
            InfoType info = new InfoType();

            // Diesem Objekt wird der Code zugewiesen
            info.setCode(code);

            // Wenn code 1 ist (Abgelehnt), dann wird eine NegativeReport-Antwort vorbereitet,
            // ansonsten eine PositiveReport-Antwort
            if (code == BigInteger.valueOf(1)) {
                // TextGerman-Prozessvariable dem Info-Type-Objekt zuweisen
                info.setTextGerman((String) processEndVariables.get("textGerman"));

                // Ein neues NegativeReportType-Objekt wird initialisiert
                NegativeReportType negativeReport = new NegativeReportType();

                // Diesem Objekt wird das InfoType-Objekt als GeneralError-Objekt zugewiesen
                negativeReport.getGeneralError().add(info);

                // Der NegativeReport wird dem Antwort-Objekt zugewiesen
                deliveryResponse.setNegativeReport(negativeReport);

                // Im Header der Antwort wird die Aktion auf 8 (=Negative Antwort) gesetzt
                headerResponse.setAction("8");

                // Im Header der Antwort wird der Nachrichtentyp auf NegativeReport gesetzt
                headerResponse.setMessageType("sedex://negativeReport");
            } else {
                // Ein neues PositiveReportType-Objekt wird initialisiert
                PositiveReportType positiveReport = new PositiveReportType();

                // Diesem Objekt wird das InfoType-Objekt als GeneralResponse-Objekt zugewiesen
                positiveReport.getGeneralResponse().add(info);

                // Der PositiveReport wird dem Antwort-Objekt zugewiesen
                deliveryResponse.setPositiveReport(positiveReport);

                // Im Header der Antwort wird die Aktion auf 9 (=Positive Antwort) gesetzt
                headerResponse.setAction("9");

                // Im Header der Antwort wird der Nachrichtentyp auf PositiveReport gesetzt
                headerResponse.setMessageType("sedex://positiveReport");
            }
        } else {
            // Ein neues InfoType-Objekt wird initialisiert
            InfoType info = new InfoType();

            // Diesem Objekt wird der Fehler-Code 1 (Abgelehnt) zugewiesen
            info.setCode(BigInteger.valueOf(1));

            // Diesem Objekt wird eine Fehlermeldung zugewiesen, dass der falsche Nachrichtentyp übergeben wurde
            info.setTextGerman("MessageType sedex://platformMoveOut, sedex://platformMoveIn oder sedex://platformMoveData erwartet, aber "
                    + headerRequest.getMessageType() + " erhalten.");

            // Ein neues NegativeReportType-Objekt wird initialisiert
            NegativeReportType negativeReport = new NegativeReportType();

            // Diesem Objekt wird das InfoType-Objekt als GeneralError-Objekt zugewiesen
            negativeReport.getGeneralError().add(info);

            // Das Objekt wird dem Antwort-Objekt zugewiesen
            deliveryResponse.setNegativeReport(negativeReport);

            // Im Header der Antwort wird die Aktion auf 8 (=Negative Antwort) gesetzt
            headerResponse.setAction("8");

            // Im Header der Antwort wird der Nachrichtentyp auf NegativeReport gesetzt
            headerResponse.setMessageType("sedex://negativeReport");
        }

        // Der Header wird der Antwort zugewiesen
        deliveryResponse.setDeliveryHeader(headerResponse);

        // Das Antwort-Objekt wird an die Webservice-Schnittstelle zurück gegeben
        return deliveryResponse;
    }
}
