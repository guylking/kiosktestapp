package com.fne.kiosk.kiosk.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="KIOSK_WORKFLOWROUTES")
public class CamelRouteComposite {

    static Logger log = LogManager.getLogger( CamelRouteComposite.class );

    @Id
    @Column(name="workflowname")
    private String name;

    @Column(name="workflowroute")
    private String route;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public CamelRouteComposite(String name, String route) {
        this.name = name;
        this.route = route;
    }

    public CamelRouteComposite() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CamelRouteComposite)) return false;
        CamelRouteComposite that = (CamelRouteComposite) o;
        return getName().equals(that.getName()) && getRoute().equals(that.getRoute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getRoute());
    }
}
