/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec;

import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.Payment;
import com.pemass.persist.enumeration.PaymentTypeEnum;

/**
 * @Description: InvoiceService
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 15:04
 */
public interface PaymentService {


    /**
     * 增加订单的支付明细
     *
     *  @param order
     * */
    Payment insertOrderPayment(Order order);

    /**
     * 增加订单支付明细，携带更多的参数
     *
     * @param order                     订单
     * @param paymentType               支付类型
     * @param externalPaymentIdentifier 外部支付号
     * @param posSerial                 支付设备序号
     * @param deviceId                  设备ID
     * @param isCustomizationOrder      是否为自定义订单
     * @return
     */
    Payment insertOrderPayment(Order order, PaymentTypeEnum paymentType, String externalPaymentIdentifier, String payId,String posSerial, Integer deviceId, Integer isCustomizationOrder, Integer isSucceed);

}
