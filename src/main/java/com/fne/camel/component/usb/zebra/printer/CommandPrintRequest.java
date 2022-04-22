package com.fne.camel.component.usb.zebra.printer;

public class CommandPrintRequest {

	
	private String coorelationId, printMessage;

	public String getCoorelationId() {
		return coorelationId;
	}

	public void setCoorelationId(String coorelationId) {
		this.coorelationId = coorelationId;
	}

	public String getPrintMessage() {
		return printMessage;
	}

	public void setPrintMessage(String printMessage) {
		this.printMessage = printMessage;
	}

	public CommandPrintRequest(String coorelationId, String printMessage) {
		super();
		this.coorelationId = coorelationId;
		this.printMessage = printMessage;
	}

	public CommandPrintRequest() {
		super();
		coorelationId="";
		printMessage="";
	}
	
	
}
