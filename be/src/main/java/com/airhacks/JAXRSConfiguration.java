package com.airhacks;

import javax.jms.JMSConnectionFactoryDefinition;
import javax.jms.JMSDestinationDefinition;
import javax.resource.ConnectionFactoryDefinition;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures a JAX-RS endpoint. Delete this class, if you are not exposing
 * JAX-RS resources in your application.
 *
 * @author airhacks.com
 */
@ApplicationPath("resources")
@ConnectionFactoryDefinition(
        name = "java:app/activemq/factory",
        interfaceName = "javax.jms.ConnectionFactory",
        resourceAdapter = "activemq-rar-5.12.0",
        properties = { "ServerUrl=tcp://127.0.0.1:61616", "UserName=admin", "Password=admin" }
)
public class JAXRSConfiguration extends Application {

}
