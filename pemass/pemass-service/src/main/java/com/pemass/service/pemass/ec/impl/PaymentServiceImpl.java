/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec.impl;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.Payment;
import com.pemass.persist.enumeration.PaymentTypeEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.ec.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: PaymentServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-07-17 16:31
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private SequenceService sequenceService;

    @Override
    public Payment insertOrderPayment(Order order) {
        return insertOrderPayment(order, order.getPaymentType(), null,null, null, null, null,null);
    }

    @Override
    public Payment insertOrderPayment(Order order, PaymentTypeEnum paymentType, String externalPaymentIdentifier, String payId, String posSerial, Integer deviceId, Integer isCustomizationOrder,Integer isSucceed) {
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setOrderIdentifier(order.getOrderIdentifier());
        payment.setPayId(payId);
        payment.setPaymentTime(new Date());
        payment.setTotalPrice(order.getTotalPrice());
        payment.setPaymentType(order.getPaymentType());
        payment.setIsCustomizationOrder(isCustomizationOrder);
        payment.setPaymentIdentifier(sequenceService.obtainSequence(SequenceEnum.PAYMENT));
        payment.setExternalPaymentIdentifier(externalPaymentIdentifier);
        payment.setPosSerial(posSerial);
        payment.setDeviceId(deviceId);
        payment.setIsSucceed(isSucceed);
        payment.setPaymentType(paymentType);
        jpaBaseDao.persist(payment);
        return payment;
    }
}

