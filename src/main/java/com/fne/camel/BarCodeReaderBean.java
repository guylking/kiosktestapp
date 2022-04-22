package com.fne.camel;

import java.util.Stack;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BarCodeReaderBean implements Processor {

	static Logger log = LogManager.getLogger( BarCodeReaderBean.class );

	public static Stack<String> barcodeReaderIntakeStack = new Stack<>();
	

	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug( "Made it to proces " + exchange.getIn().getBody());
		
		
		barcodeReaderIntakeStack.push("" + exchange.getIn().getBody());
		System.out.println( "" + exchange.getIn().getBody() );
	}

	
	public void displayContent(Exchange exchange) throws Exception {
		
	}
	
}
