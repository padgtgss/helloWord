/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.jms.impl;

import com.pemass.service.jms.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.*;
import java.io.IOException;

/**
 * @Description: JMS 邮件生产者
 * @Author: estn.zuo
 * @CreateTime: 2014-12-11 21:17
 */
@Component("mailProducer")
public class MailProducer implements Producer {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Destination logDestination = new ActiveMQQueue("pemass.mail.destination");

    @Resource
    private JmsTemplate jmsTemplate;

    @Override
    public void send(final Object jmsMessage) {
        jmsTemplate.send(logDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    return session.createObjectMessage(objectMapper.writeValueAsString(jmsMessage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
