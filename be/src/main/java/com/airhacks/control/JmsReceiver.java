package com.airhacks.control;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import io.reactivex.FlowableEmitter;

@MessageDriven(name = "testmdb", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "TESTQ"),
        @ActivationConfigProperty(propertyName = "resourceAdapter", propertyValue = "activemq-rar-5.12.0")
})
public class JmsReceiver implements MessageListener {

    @Inject
    @FlowableProcessor
    FlowableEmitter<String> emitter;

    @Resource
    ManagedExecutorService mes;

    @Override
    public void onMessage(Message message) {
        try {
            final String mess = ((TextMessage) message).getText();
            System.out.println("Got message " + mess);
            mes.submit(() -> emitter.onNext(mess));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
