package com.fne.camel;

import java.util.Stack;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CashAcceptorBean implements Processor {

	static Logger log = LogManager.getLogger( CashAcceptorBean.class );

	public static Stack<String> cashReaderIntakeStack = new Stack<>();
	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug( "Made it to proces " + exchange.getIn().getBody());
		cashReaderIntakeStack.push("" + exchange.getIn().getBody());
		System.out.println( "" + exchange.getIn().getBody() );

	}

}
