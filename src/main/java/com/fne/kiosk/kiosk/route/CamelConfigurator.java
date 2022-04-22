package com.fne.kiosk.kiosk.route;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfigurator {

    static Logger log = LogManager.getLogger( CamelConfigurator.class );

    @Bean( name={"CTRRouteBuilder"})
    RouteLoader getBDXRouteLoader() {
        return( RouteLoader.getInstance() );
    }

    @Bean( name="{idempotentRepo}")
    MemoryIdempotentRepository getIdeepotentRepo() {
        return( new MemoryIdempotentRepository());
    }

}
