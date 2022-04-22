package com.fne.camel.component.usb.zebra.printer;

public enum PrinterCommands {

	FILECOMMAND( 1, "FileCommand", "Command to dowload a file to a printer"),
	PRINTCOMMAND( 2, "Print Message Command", "Command to print message to printer" ),
	UNKNOWN( -1, "Command Unknown", "Command is Unknown");
	
	
	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public String getStrCommand() {
		return strCommand;
	}

	public void setStrCommand(String strCommand) {
		this.strCommand = strCommand;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private int command;
	private String strCommand;
	private String desc;
	
	private PrinterCommands( int command, String strCommand, String desc ) {
		
		this.command = command;
		this.strCommand = strCommand;
		this.desc = desc;
	}
	
	public static PrinterCommands getCommand( int cmd ) {
		
		if( cmd == FILECOMMAND.getCommand())
			return(FILECOMMAND);
		
		if( cmd == PRINTCOMMAND.getCommand())
			return(PRINTCOMMAND);
		
		return UNKNOWN;
	}
}
