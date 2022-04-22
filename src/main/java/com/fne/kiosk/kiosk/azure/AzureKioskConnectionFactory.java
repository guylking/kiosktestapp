package com.fne.kiosk.kiosk.azure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.connection.CachingConnectionFactory;


import com.microsoft.azure.spring.autoconfigure.jms.ConnectionStringResolver;
import com.microsoft.azure.spring.autoconfigure.jms.ServiceBusKey;


public class AzureKioskConnectionFactory extends CachingConnectionFactory implements InitializingBean  {

	

	static Logger log = LogManager.getLogger( AzureKioskConnectionFactory.class );


	@Override
    public void afterPropertiesSet() {

        String conn = connectionString;     
        log.debug( "Connection string is " + conn );

        ServiceBusKey serviceBusKey = ConnectionStringResolver.getServiceBusKey(conn);
        String host = serviceBusKey.getHost();
        String sasKeyName = serviceBusKey.getSharedAccessKeyName();
        String sasKey = serviceBusKey.getSharedAccessKey();

        String remoteUri = String.format(AMQP_URI_FORMAT, host, idleTimeout);
        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();
        jmsConnectionFactory.setRemoteURI(remoteUri);
        jmsConnectionFactory.setUsername(sasKeyName);
        jmsConnectionFactory.setPassword(sasKey);
        
        this.setTargetConnectionFactory(jmsConnectionFactory );
        this.setReconnectOnException(true);
 
	}
	
    private String connectionString;
    private int idleTimeout;
    
    
    

    public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	private static final String AMQP_URI_FORMAT = "amqps://%s?amqp.idleTimeout=%d";

    public AzureKioskConnectionFactory() {

   }
}