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
 * @Description: JMS Log日志生产者
 * @Author: estn.zuo
 * @CreateTime: 2014-12-11 21:08
 */
@Component("clearingProducer")
public class ClearingProducer implements Producer {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Destination clearingDestination = new ActiveMQQueue("pemass.clearing.destination");

    @Resource
    private JmsTemplate jmsTemplate;

    @Override
    public void send(final Object jmsMessage) {
        /*-- 直接将message序列化为josn字符串输出 --*/
            jmsTemplate.send(clearingDestination, new MessageCreator() {
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
