package com.fne.kiosk.kiosk;


import java.util.UUID;

import com.fne.camel.BarCodeReaderBean;
import com.fne.camel.CardReadBean;
import com.fne.camel.CashAcceptorBean;
import com.fne.camel.CheckReaderBean;
import com.fne.camel.ZebraPrinterBean;
import com.fne.camel.component.usb.zebra.printer.CommandAddFile;
import com.fne.camel.component.usb.zebra.printer.CommandPrintRequest;
import com.fne.camel.component.usb.zebra.printer.CommandUnion;
import com.fne.camel.component.usb.zebra.printer.PrinterCommands;
import com.google.gson.Gson;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * The main view contains a button and a click listener.
 */



@Push
@Route("/mainview")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout implements AppShellConfigurator  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8665920185276589186L;
	public  TextArea textArea = null;
	public  TextArea cashAcceptor = null;
	public  TextArea barcodeReader = null;
	public  TextArea checkReader = null;
	
    private FeederThread thread;
	  
	  
   @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
        thread.interrupt();
        thread = null;
    }
	   
	@Override
    protected void onAttach(AttachEvent attachEvent) {
 //       add(new Span("Waiting for updates"));

        // Start the data feed thread
        thread = new FeederThread(attachEvent.getUI(), this);
        thread.start();
    }
	
	public void clearAllViews() {
		this.textArea.clear(); 
		this.cashAcceptor.clear();
		this.barcodeReader.clear();
		this.checkReader.clear();
	}
	
	public String getZebraPrintMessage( TextField text ) {
		
		String ret = "^XA^FO105,020 ^XGE:LOGO.BMP,1,1 ^FS^FO100,250^A0N,32,32^FDStark County Government^FS^FO100,290^A0N,32,32^FD110 Central Plaza South^FS^FO100,330^A0N,32,32^FDCanton, OH 44702^FS^FO100,370^A0N,32,32^FD ^FS^FO100,410^A0N,32,32^FD@dept @depPh^FS^FO100,450^A0N,32,32^FD^FS^FO100,490^A0N,32,32^FD@kiosk^FS^FO100,530^A0N,32,32^FD ^FS^FO100,570^A0N,32,32^FD@accto^FS^FO100,610^A0N,32,32^FD@accti^FS^FO100,650^A0N,32,32^FD@acctj^FS^FO100,690^A0N,32,32^FD@acctk^FS^FO100,730^A0N,32,32^FD@acctl^FS^FO100,770^A0N,32,32^FD ^FS^FO100,810^A0N,32,32^FDSubtotal: @sub^FS^FO100,850^A0N,32,32^FDFees: @fee^FS^FO100,890^A0N,32,32^FDTotal: @total^FS^FO100,930^A0N,32,32^FD ^FS^FO100,970^A0N,32,32^FDPaid by @payMeth^FS^FO100,1010^A0N,32,32^FD@name^FS^FO100,1050^A0N,32,32^FD@phone^FS^FO100,1090^A0N,32,32^FD@email^FS^FO100,1130^A0N,32,32^FDThank you for using the Stark County^FS^FO100,1170^A0N,32,32^FDSelf-Service Payment Kiosk^FS^CN1^PN0^XZ";
		
		String v = text.getValue();
		
		if( ( v.length() > 0 ) && ( v.startsWith("^XA")) ) {
			ret = v;
		}

		CommandUnion x = new CommandUnion(
     			PrinterCommands.PRINTCOMMAND.getCommand(), 
     			new CommandAddFile(), 
     			new CommandPrintRequest( UUID.randomUUID().toString(), ret ) );
		
 		Gson gson = new Gson();
 		ret = gson.toJson( x );
		return( ret );
	}
	
    public MainView() {
    	
    	this.setWidthFull();
        // Use TextField for standard text input
        TextField textField = new TextField("Zebra Print Message");

        // Button click listeners can be defined as lambda expressions
        GreetService greetService = new GreetService();
        Button button = new Button("Say hello",
                e ->  Notification.show(greetService.greet(textField.getValue())));

        Button button2 = new Button("Clear All Views",
                e -> clearAllViews() );
        
//        StringBuffer mess = new StringBuffer();
//        mess.append("^XA^FO105,020 ^XGE:LOGO.BMP,1,1 ^FS^FO100,250^A0N,32,32^FDStark County Government^FS^FO100,290^A0N,32,32^FD110 Central Plaza South^FS^FO100,330^A0N,32,32^FDCanton, OH 44702^FS^FO100,370^A0N,32,32^FD ^FS").append("^FO100,410^A0N,32,32^FD@dept @depPh^FS^FO100,450^A0N,32,32^FD^FS^FO100,490^A0N,32,32^FD@kiosk^FS^FO100,530^A0N,32,32^FD ^FS^FO100,570^A0N,32,32^FD@accto^FS").append("^FO100,610^A0N,32,32^FD@accti^FS^FO100,650^A0N,32,32^FD@acctj^FS^FO100,690^A0N,32,32^FD@acctk^FS^FO100,730^A0N,32,32^FD@acctl^FS^FO100,770^A0N,32,32^FD ^FS").append("^FO100,810^A0N,32,32^FDSubtotal: @sub^FS^FO100,850^A0N,32,32^FDFees: @fee^FS^FO100,890^A0N,32,32^FDTotal: @total^FS^FO100,930^A0N,32,32^FD ^FS^FO100,970^A0N,32,32^FDPaid by @payMeth^FS").append("^FO100,1010^A0N,32,32^FD@name^FS^FO100,1050^A0N,32,32^FD@phone^FS^FO100,1090^A0N,32,32^FD@email^FS^FO100,1130^A0N,32,32^FDThank you for using the Stark County^FS").append("^FO100,1170^A0N,32,32^FDSelf-Service Payment Kiosk^FS^CN1^PN0^XZ");

		/*
		 * CommandUnion x = new CommandUnion( PrinterCommands.PRINTCOMMAND.getCommand(),
		 * new CommandAddFile(), new CommandPrintRequest( UUID.randomUUID().toString(),
		 * mess.toString()) );
		 */// 		CommandUnion y = new CommandUnion(PrinterCommands.FILECOMMAND.getCommand(), new CommandAddFile( UUID.randomUUID().toString(), "label", "b64String"), new CommandPrintRequest( ));
 		
        Button button3 = new Button("Print Message",
                e -> ZebraPrinterBean.process( getZebraPrintMessage(textField)) );
        
        
        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button3.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);
        button2.addClickShortcut(Key.ENTER);
        button3.addClickShortcut(Key.ENTER);
   
        
        textArea = new TextArea( "Credit Card Events" );
        textArea.setWidth( "100%" );
        textArea.setReadOnly( true );
        textArea.setMinWidth("500px");
        textArea.setMinHeight("100px");
        textArea.setMaxHeight("100px");
        textArea.setWidthFull();
        textArea.setVisible(true);
        
    	cashAcceptor = new TextArea( "Cash Acceptor Events" );
    	cashAcceptor.setWidth( "100%" );
    	cashAcceptor.setReadOnly( true );
    	cashAcceptor.setMinWidth("500px");
    	cashAcceptor.setMinHeight("100px");
    	cashAcceptor.setMaxHeight("100px");
    	cashAcceptor.setWidthFull();
    	
    	barcodeReader = new TextArea( "BarCode Events" );
    	barcodeReader.setWidth( "100%" );
    	barcodeReader.setReadOnly( true );
    	barcodeReader.setMinWidth("500px");
    	barcodeReader.setMinHeight("100px");
    	barcodeReader.setMaxHeight("100px");
    	barcodeReader.setWidthFull();
    	
    	checkReader= new TextArea( "Check Reader Events" );
    	checkReader.setWidth( "100%" );
    	checkReader.setReadOnly( true );
    	checkReader.setMinWidth("500px");
    	checkReader.setMinHeight("100px");
    	checkReader.setMaxHeight("100px");
    	checkReader.setWidthFull();
    	checkReader.setVisible(true);
        
    	textField.setMinWidth("500px");
        HorizontalLayout hor = new HorizontalLayout();
        hor.add(textField, button, button2, button3);
        
        
        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        //addClassName("centered-content");

        add(textArea, cashAcceptor, barcodeReader, checkReader,  hor);
    }
    
    private static class FeederThread extends Thread {
        private final UI ui;
        private final MainView view;

        private int count = 0;

        public FeederThread(UI ui, MainView view) {
            this.ui = ui;
            this.view = view;
        }

        @Override
        public void run() {
            try {
                // Update the data for a while
            	while(!Thread.interrupted()) {
            		// Sleep to emulate background work
                    Thread.sleep(500);
                    if(! CardReadBean.cardReaderIntakeStack.isEmpty() ) {
                        String value = CardReadBean.cardReaderIntakeStack.pop();
                       	ui.access(() -> view.textArea.setValue( view.textArea.getValue() + value + "\n" ));
//                       	ui.access(() -> MainView.textArea.setValue( MainView.textArea.getValue() + value + "\n" ));
                    }
                    
                    if(! CashAcceptorBean.cashReaderIntakeStack.isEmpty() ) {
                        String value = CashAcceptorBean.cashReaderIntakeStack.pop();
                       	ui.access(() -> view.cashAcceptor.setValue( view.cashAcceptor.getValue() + value  + "\n" ));
                    }
                    
                    if(! BarCodeReaderBean.barcodeReaderIntakeStack.isEmpty() ) {
                        String value = BarCodeReaderBean.barcodeReaderIntakeStack.pop();
                       	ui.access(() -> view.barcodeReader.setValue( view.barcodeReader.getValue() + value  + "\n" ));
                    }
                    
                    if(! CheckReaderBean.checkReaderIntakeStack.isEmpty() ) {
                        String value = CheckReaderBean.checkReaderIntakeStack.pop();
                        ui.access(() -> view.checkReader.setValue( view.checkReader.getValue() + value  + "\n" ));
//                       	ui.access(() -> MainView.checkReader.setValue( MainView.checkReader.getValue() + value  + "\n" ));
                    }
                    
                }

                // Inform that we are done
				/*
				 * ui.access(() -> { view.add(new Span("Done updating")); });
				 */            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }    
}

/*
public class MainView extends VerticalLayout {

    public MainView() {
        // Use TextField for standard text input
        TextField textField = new TextField("Your name");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello",
                e -> Notification.show("Hello"));

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, button);
    }
}
*/