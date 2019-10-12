package com.airhacks.control;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.resource.AdministeredObjectDefinition;
import javax.resource.ConnectionFactoryDefinition;

@Stateless
@ConnectionFactoryDefinition(name = "java:global/jms/SendJMS",
        interfaceName = "javax.jms.ConnectionFactory",
        resourceAdapter = "activemq-rar-5.12.0",
        properties = {"UserName=admin", "Password=admin", "ServerUrl=tcp://127.0.0.1:61616"})
@AdministeredObjectDefinition(resourceAdapter = "activemq-rar-5.12.0",
        interfaceName = "javax.jms.Queue",
        className = "org.apache.activemq.command.ActiveMQQueue",
        name = "java:global/jms/TestQ",
        properties = {"PhysicalName=TESTQ"})
public class JmsSender {

    @Resource(lookup = "java:global/jms/TestQ")
    Queue queue;

    @Resource(lookup = "java:global/jms/SendJMS")
    ConnectionFactory factory;

    @Schedule(hour = "*", minute = "*", second = "*/1", info = "Every 5 second timer", timezone = "UTC", persistent = false)
    public void myTimer() {
        try (Connection conn = factory.createConnection()) {
            Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
            sess.createProducer(queue).send(sess.createTextMessage("("+ UUID.randomUUID() +") This is a test at " + new Date()));
        } catch (JMSException ex) {
            //Logger.getLogger(SendJMSMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
