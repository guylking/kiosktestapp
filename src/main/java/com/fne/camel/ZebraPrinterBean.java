package com.fne.camel;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.model.ModelCamelContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class ZebraPrinterBean {

	static Logger log = LogManager.getLogger( ZebraPrinterBean.class );

	public static void process( String message ) {
		
		log.debug( "Insde Zebra Printer Bean process method" );

		try {
			ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			ModelCamelContext camelContext = (ModelCamelContext) context.getBean("mainCamelContext");
			camelContext.createProducerTemplate().asyncSendBody("direct:sendToZebra", message);
		} catch (CamelExecutionException e) {
			log.error( e.getLocalizedMessage() );
		}
		
	}
}
