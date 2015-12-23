/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.jms;


/**
 * @Description: Producer
 * @Author: estn.zuo
 * @CreateTime: 2014-12-11 21:06
 */
public interface Producer {
    /**
     * JMS 消息生产者
     * @param jmsMessage
     */
    void send(Object jmsMessage);
}
