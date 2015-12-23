/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.redis.OrderCode;

/**
 * @Description: OrderService
 * @Author: lin.shi
 * @CreateTime: 2015-09-28 10:36
 */
public interface OrderCodeService {

    /**
     * 根据订单编码获取订单验证码信息
     * @param orderIdentifier
     * @return
     */
    OrderCode get(String orderIdentifier);

    OrderCode save(String orderIdentifier);

    Order validate(String  orderCode);
}