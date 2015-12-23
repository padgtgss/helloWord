/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys;

import com.pemass.persist.domain.redis.OrderPay;

/**
 * @Description: OrderPayDao
 * @Author: lin.shi
 * @CreateTime: 2015-09-22 11:04
 */
public interface OrderPayDao {

    /**
     * 根据支付码 获取对象
     * @param payCode
     * @return
     */
    OrderPay getOrderPayById(String payCode);

    /**
     * 保存对象
     * @param orderPay
     * @return
     */
    Boolean save(OrderPay orderPay);


    void delOrderPay(OrderPay orderPay);
}