/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.jms.impl;

import com.pemass.service.jms.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * @Description: IssuePackProducer
 * @Author: oliver.he
 * @CreateTime: 2015-09-10 14:31
 */
@Component("issuePackProducer")
public class IssuePackProducer implements Producer {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Log logger = LogFactory.getLog(getClass());

    private Destination issuePackDestination = new ActiveMQQueue("pemass.issuePack.destination");

    @Resource
    private JmsTemplate jmsTemplate;

    @Override
    public void send(final Object jmsMessage) {
        /*-- 直接将message序列化为json字符串输出 --*/
        jmsTemplate.send(issuePackDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    return session.createTextMessage(objectMapper.writeValueAsString(jmsMessage));
                } catch (IOException e) {
                    logger.error(e);
                }
                return null;
            }
        });
    }

}