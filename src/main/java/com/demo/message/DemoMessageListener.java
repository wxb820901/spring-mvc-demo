package com.demo.message;

import com.demo.jpa.entity.JsonObject;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by wangbil on 5/10/2017.
 */
@Component
public class DemoMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                System.out.println(((TextMessage) message).getText());
            }catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }else {
            System.out.println( message);
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(JsonObject email) {
        System.out.println("Received <" + email + ">");
    }

}
