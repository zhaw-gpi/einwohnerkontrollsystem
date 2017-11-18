/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.sml.iwi.gpi.musterloesung.eks.configuration;

import ch.zhaw.sml.iwi.gpi.musterloesung.eks.controller.EinwohnerKontrollSystemController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author scep
 */
@Configuration
public class ApplicationConfiguration {
    
    @Bean
    public EinwohnerKontrollSystemController getEinwohnerKontrollSystemController(){
        return new EinwohnerKontrollSystemController();
    }
    
}
