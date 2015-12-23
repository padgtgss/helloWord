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
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;


/**
 * @Description: JMS 短信生产者
 * @Author: estn.zuo
 * @CreateTime: 2014-12-11 21:17
 */
@Component("smsProducer")
public class SmsProducer implements Producer {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Destination smsDestination = new ActiveMQQueue("pemass.sms.destination");

    @Resource
    private JmsTemplate jmsTemplate;

    @Override
    public void send(final Object jmsMessage) {

        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    return session.createTextMessage(objectMapper.writeValueAsString(jmsMessage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
