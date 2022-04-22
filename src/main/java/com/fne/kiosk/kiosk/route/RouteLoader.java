package com.fne.kiosk.kiosk.route;


import org.apache.camel.*;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedOperation;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.model.ModelCamelContext;

import org.apache.camel.model.RoutesDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.fne.kiosk.kiosk.config.AppConfig;
import com.fne.kiosk.kiosk.model.CamelRouteComposite;
import com.fne.kiosk.kiosk.model.CamelRouteDetailsWithHash;
import com.fne.kiosk.kiosk.repository.JpaRouteRepository;
import com.microsoft.azure.spring.autoconfigure.jms.ServiceBusKey;


@ManagedResource( description = "Managed Route Loader")
public class RouteLoader implements CamelContextAware, RoutesBuilder, Processor, Service {

    static Logger log = LogManager.getLogger( RouteLoader.class );

    private static RouteLoader myself = new RouteLoader();
    private static String routeBase;
    private ModelCamelContext camelctx;

    private static Map<String, CamelRouteDetailsWithHash> routeMape = new HashMap<>();


    @Autowired
    private AppConfig appconfig;

    @Autowired
    private JpaRouteRepository routeRepo;
    

    private RouteLoader(){

    }


    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelctx = (ModelCamelContext) camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelctx;
    }

    @Override
    public void addRoutesToCamelContext(CamelContext context) throws Exception {

        if( camelctx != null )
            camelctx = (ModelCamelContext) context;

 //       loadAllRoutes();
    }

    public static RouteLoader getInstance() {
        if( RouteLoader.myself == null )
            RouteLoader.myself = new RouteLoader();

        return( RouteLoader.myself );
    }

    @ManagedAttribute
    public String getRouteBase() {
        return RouteLoader.routeBase;
    }

    @ManagedAttribute
    public void setRouteBase( String routeBase ) {
        RouteLoader.routeBase = routeBase;
    }

    @ManagedOperation
    public void loadAllRoutes( String fromBase ) throws  Exception {

        RouteLoader.routeBase = fromBase;
        loadAllRoutes();
    }

    /*
      This method is used for loading routes from a files system and may be used
      for some testing with most routes comping from a database.
     */
    public void loadAllRoutes() throws  Exception {

        File[] allFiles = null;
        File folder = new File( routeBase );

        if( folder != null )
            allFiles = folder.listFiles();

        if( allFiles != null ) {
            if( allFiles.length == 0 )
                log.debug(  "### No Routes found ###");
            else {
                log.debug( "### We have routes ###");

                for( int count = 0; count < allFiles.length; count ++) {
                    File f = allFiles[count];
                    FileInputStream fis = new FileInputStream( f );

                    log.debug( "Processing File " + f.getName());

                    RoutesDefinition routes = (RoutesDefinition) ((ExtendedCamelContext) camelctx).getXMLRoutesDefinitionLoader().loadRoutesDefinition(camelctx, fis);

                    camelctx.addRouteDefinitions(  routes.getRoutes() );
                    fis.close();
                }
            }
        } else {
            log.error( "### No Routes found in location " + routeBase + ", Location doesn't exist ###");
        }
    }

    public void checkforRouteChanges(CamelRouteComposite routes ) throws Exception {


        try {
            if( RouteLoader.routeMape.containsKey(routes.getName() )) {
                CamelRouteDetailsWithHash mapRoute = RouteLoader.routeMape.get( routes.getName() );
                if( routes.getRoute().hashCode() == mapRoute.getHashCode() ) {
                    log.info( "The routes are the same");
                } else {
                    log.debug( "The Routes have changed");
                    camelctx.removeRouteDefinitions( RouteLoader.routeMape.get( routes.getName()).getDefinition().getRoutes()  );
                    RouteLoader.routeMape.remove(routes.getName());

                    RoutesDefinition routeName = (RoutesDefinition) ((ExtendedCamelContext) camelctx).getXMLRoutesDefinitionLoader().loadRoutesDefinition(camelctx, new ByteArrayInputStream(routes.getRoute().getBytes()));

                    RouteLoader.routeMape.put(routes.getName(), new CamelRouteDetailsWithHash( routeName, routes.getRoute().hashCode()));
                }
            }
        } catch( Exception e ) {
            log.error( e );
        }
    }

    @ManagedOperation
    public void loadRoutes( CamelRouteComposite route ) throws Exception {

        log.debug( "### Inside Load Routes ");

        try {
            if( ! RouteLoader.routeMape.containsKey( route.getName() )) {
                RoutesDefinition routeName = (RoutesDefinition) ((ExtendedCamelContext) camelctx).getXMLRoutesDefinitionLoader( ).loadRoutesDefinition( camelctx,
                        new ByteArrayInputStream( route.getRoute().replaceAll("\\{\\{ORCH_TEMPLATE\\}\\}", appconfig.orechstrationserviceurl()).getBytes()));
                camelctx.addRouteDefinitions( routeName.getRoutes() );
                log.debug( "###  Adding Route to Camel Context ###");
                RouteLoader.routeMape.put( route.getName(), new CamelRouteDetailsWithHash( routeName, route.getRoute().hashCode()));
            }
        } catch( Exception e ) {
            log.error( e );
        }
    }

    private void removeDeletedRoutes( Exchange exchange ) throws Exception{

        List<CamelRouteComposite> allRoutes = (List<CamelRouteComposite>) exchange.getIn().getBody();

        // Process
        // 1. Retrieve the map of current routes.
        // 2. Retrieve the list of routes from Store.
        // 3. Using the map of routes, determine if any routes are missing from the store.
        // 4. for those routes missing do the following:
        //    i. Remove the route from the map
        //    2. Unload the route from the camel context.

        log.debug( "Remove undesirable routes ");
        List<String> removallKeys = new LinkedList<String>();

        RouteLoader.routeMape.forEach( (k,v) -> {
            try {
                Predicate<? super CamelRouteComposite> contained = itemComp -> { return( itemComp.getName().compareToIgnoreCase( k.trim()) == 0 ); };

                boolean isFound = allRoutes.stream().anyMatch(contained);

                log.debug( "Found state is " + isFound );

                if( !isFound ) {
                    log.debug( "Ready to remove route " + k );
                    removallKeys.add( k );
                    camelctx.removeRouteDefinitions( v.getDefinition().getRoutes() );
                }
            } catch( Exception e ) {
                log.error( e.getMessage() );
            }
        });

        for( String k : removallKeys ) {
            RouteLoader.routeMape.remove( k );
        }
    }

    public void checkForDeletedEntries( Exchange exchange ) throws Exception {
        removeDeletedRoutes( exchange );
    }


    @Override
    public void process( Exchange exchange ) throws  Exception  {

    }

    public void addRoutesFromStore( Exchange exchange) throws Exception {

        log.debug( "### Inside Adding Routes ###" );
        List<CamelRouteComposite> allRoutes = ( List<CamelRouteComposite>) exchange.getIn().getBody();

        for( CamelRouteComposite route : allRoutes ) {
            log.debug( "### Adding Route " + route.getName() );
            loadRoutes( route );
        }

        log.debug( "Adding routes from store ");
    }

    public void checkRoutesForStoreUpdate( Exchange exchange ) throws Exception {

        List<CamelRouteComposite> allRoutes = ( List<CamelRouteComposite>) exchange.getIn().getBody();

        for( CamelRouteComposite route : allRoutes ) {
            checkforRouteChanges( route );
        }

        log.debug( "Checking for route updates");
    }

    public void readAllRoutesFromStore( Exchange exchange ) throws  Exception {

    	
//    	ServiceBusKey result = com.microsoft.azure.spring.autoconfigure.jms.ConnectionStringResolver.getServiceBusKey("Endpoint=sb://fneservicebustest.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=7AYDx+nIJdUEUD2OqfpPEdpAudPrNMHtGxb7quecXrE=");

//    	System.out.println("Host is " + result.getHost() );
//    	System.out.println("Shared access key is " + result.getSharedAccessKey() );
    	
    	
        log.debug( "### Reading all routes from store ###");
        exchange.getIn().setHeader("BDXStatus", "New Route");

        List<CamelRouteComposite> res = routeRepo.findAll();

        exchange.getIn().setBody( routeRepo.findAll() );
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


}
