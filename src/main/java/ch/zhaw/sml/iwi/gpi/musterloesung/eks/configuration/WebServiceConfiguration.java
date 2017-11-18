/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.sml.iwi.gpi.musterloesung.eks.configuration;

import ch.zhaw.sml.iwi.gpi.musterloesung.eks.endpoint.EinwohnerKontrollSystemService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author scep
 */
@Configuration
public class WebServiceConfiguration {
    
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new CXFServlet(), "/soap/*");
    }
    
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus(){
        return new SpringBus();
    }
    
    @Bean
    public EinwohnerKontrollSystemService einwohnerKontrollSystemService(){
        return new EinwohnerKontrollSystemService();
    }
    
    
}
