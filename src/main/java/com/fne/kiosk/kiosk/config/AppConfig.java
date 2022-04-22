package com.fne.kiosk.kiosk.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan
public class AppConfig {


    @Autowired
    private Environment env;

    @Bean( name="orchestrationurl")
    public String orechstrationserviceurl() {

        return( "endpoint");

    }


}
