package com.fne.camel.component.usb.zebra.printer;


public class CommandAddFile {

	
	private String coorelationId, b64File, label;

	public String getCoorelationId() {
		return coorelationId;
	}

	public void setCoorelationId(String coorelationId) {
		this.coorelationId = coorelationId;
	}

	public String getB64File() {
		return b64File;
	}

	public void setB64File(String b64File) {
		this.b64File = b64File;
	}

	public CommandAddFile(String coorelationId, String label, String b64File) {
		super();
		this.coorelationId = coorelationId;
		this.b64File = b64File;
		this.label = label;
	}
	
	

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public CommandAddFile() {
		super();
		coorelationId="";
		b64File="";	
		label = "";
	}
	
	
}
