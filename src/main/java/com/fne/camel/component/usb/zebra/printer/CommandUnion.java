package com.fne.camel.component.usb.zebra.printer;

public class CommandUnion {

	
	private int command;
	private CommandAddFile filecmd;
	private CommandPrintRequest printcmd;
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public CommandAddFile getFilecmd() {
		return filecmd;
	}
	public void setFilecmd(CommandAddFile filecmd) {
		this.filecmd = filecmd;
	}
	public CommandPrintRequest getPrintcmd() {
		return printcmd;
	}
	public void setPrintcmd(CommandPrintRequest printcmd) {
		this.printcmd = printcmd;
	}
	public CommandUnion(int command, CommandAddFile filecmd, CommandPrintRequest printcmd) {
		super();
		this.command = command;
		this.filecmd = filecmd;
		this.printcmd = printcmd;
	}
}
