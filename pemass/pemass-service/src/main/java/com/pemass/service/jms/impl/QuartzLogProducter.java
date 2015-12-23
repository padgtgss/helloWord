package com.pemass.service.jms.impl;

import com.pemass.persist.domain.mongo.log.QuartzLogMessage;
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
 * @Description: QuartzLogProducter
 * @Author: xy
 * @CreateTime: 2015-07-31 15:53
 */
@Component("quartzLogProducer")
public class QuartzLogProducter implements Producer {
    private ObjectMapper objectMapper = new ObjectMapper();

    private Destination logDestination = new ActiveMQQueue("pemass.quartz.destination");

    @Resource
    private JmsTemplate jmsTemplate;

    @Override
    public void send(final Object jmsMessage) {
           /*-- 直接将message序列化为josn字符串输出 --*/
        if (jmsMessage instanceof QuartzLogMessage) {
            jmsTemplate.send(logDestination, new MessageCreator() {
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
}
