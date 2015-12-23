/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys;

import com.pemass.persist.domain.redis.OrderCode;

/**
 * @Description: OrderDao
 * @Author: lin.shi
 * @CreateTime: 2015-09-28 10:42
 */
public interface OrderCodeDao {

    /**
     * 根据订单码获取订单信息
     * @param orderCode
     * @return
     */
    OrderCode get(String orderCode);

    /**
     * 保存订单验证码
     * @param order
     * @return
     */
    Boolean save(OrderCode order);


    /**
     * 删除订单验证码
     * @param order
     */
    void delOrder(OrderCode order);
}