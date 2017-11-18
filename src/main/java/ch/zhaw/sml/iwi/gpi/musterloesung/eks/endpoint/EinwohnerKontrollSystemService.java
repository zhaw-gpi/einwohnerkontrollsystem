package ch.zhaw.sml.iwi.gpi.musterloesung.eks.endpoint;


import ch.ech.xmlns.ech_0194._1.DeliveryType;
import ch.zhaw.sml.iwi.gpi.musterloesung.eks.controller.EinwohnerKontrollSystemController;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Endpoint-Definition
 * Hier wird der eigentliche Web Service definert, was zur Laufzeit im
 * Hintergrund dazu führt, dass ein WSDL generiert wird und dass die Web service-
 * Operationen als Methoden bereit gestellt werden. Werden diese Operationen
 * aufgerufen, wird die jeweilige Methode aufgerufen, welche allerdings nur die
 * eigentliche Implementation aufruft, die in einem separaten Controller stattfindet
 * Die @WebService-JAX-WS-Annotation definiert diese Klasse als Web Service-Klasse
 */
@WebService(name="Einwohnerkontrollsystem-Service", portName="EinwohnerKontrollSystemServicePort", targetNamespace = "http://www.ech.ch/xmlns/eCH-0194/1")
@SchemaValidation
public class EinwohnerKontrollSystemService {
    
    /**
     * Die eigentliche Implementation soll getrennt von der Schnittstellen- 
     * Definition sein. Dies wird mit einer separaten Controller-Klasse gelöst.
     * Über @Autowired wird das zur Laufzeit verfügbare Controller-Objekt
     * in diesem Service als Variable eingefügt (Dependency Injection). PS: Damit
     * es zur Laufzeit verfügbar ist, wird es in ApplicationConfiguration.java erstellt
     */
    @Autowired
    private EinwohnerKontrollSystemController einwohnerKontrollSystemController;

    /**
     * Definition der Webservice-Operation handleDelivery
     * Diese erwartet das generische Delivery-Objekt und gibt auch ein solches
     * zurück
     * 
     * Die @WebMethod-JAX-WS-Annotation definiert eine Webservice-Operation
     * @WebParam definiert die Bezeichnung der Nachrichten-Input-Parameter
     * @WebResult definiert die Bezeichnung des Nachrichten-Output-Parameters
     * @param delivery
     * 
     * @return 
     */
    @WebMethod(operationName="handleDelivery")
    @WebResult(name="delivery")
    public DeliveryType handleDelivery(@WebParam(name = "delivery") DeliveryType delivery) {
        // Ruft die eigentliche Implementations-Methode dieser Webservice-Operation auf
        // und sendet deren Resultat zurück an den SOAP-Client
        return einwohnerKontrollSystemController.handleDelivery(delivery);
    }
}
