package com.fne.kiosk.kiosk.model;

import org.apache.camel.model.RoutesDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CamelRouteDetailsWithHash {

    static Logger log = LogManager.getLogger( CamelRouteDetailsWithHash.class );

    private RoutesDefinition definition;

    private int hashCode;

    public RoutesDefinition getDefinition( ) {
        return definition;
    }

    public void setDefinition( RoutesDefinition definition ) {
        this.definition = definition;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode( int hashCode ) {
        this.hashCode = hashCode;
    }

    public CamelRouteDetailsWithHash( RoutesDefinition definition, int hashCode ) {

        super();
        this.definition = definition;
        this.hashCode = hashCode;
    }
}
