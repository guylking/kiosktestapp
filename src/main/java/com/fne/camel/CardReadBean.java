package com.fne.camel;

import java.util.Stack;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class CardReadBean implements Processor {

	static Logger log = LogManager.getLogger( CardReadBean.class );

	public static Stack<String> cardReaderIntakeStack = new Stack<>();
	
	
//	public static String cardRead = "";
	
	public void process(Exchange exchange) throws Exception {
		log.debug( "Made it to proces " + exchange.getIn().getBody());
		
		
		cardReaderIntakeStack.push("" + exchange.getIn().getBody());
		System.out.println( "" + exchange.getIn().getBody() );
		// MainView.textArea.setValue("" + exchange.getIn().getBody());
		// MainView.textArea.setValue("This is a very long test");
	}

	
	public void displayContent(Exchange exchange) throws Exception {
		
	}
	
	

	
}
