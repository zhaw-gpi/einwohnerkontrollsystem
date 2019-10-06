package ch.zhaw.gpi.eks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hauptklasse für die SpringBoot-Applikation
 * 
 * @author scep
 */
@SpringBootApplication
@EnableAsync
public class AsyncRestMessagingExternalApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AsyncRestMessagingExternalApplication.class, args);
    }
}
