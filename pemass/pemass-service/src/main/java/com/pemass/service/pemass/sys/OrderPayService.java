/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.OrderPay;

/**
 * @Description: OrderPayService
 * @Author: lin.shi
 * @CreateTime: 2015-09-22 10:58
 */
public interface OrderPayService {

    /**
     * 根据用户ID 获取付款编码
     * @param uid
     * @return
     */
    OrderPay getById(Long uid);

    /**
     * 保存付款编码
     * @param uid
     * @return
     */
    OrderPay save(Long uid);

    /**
     * 验证付款码
     * @param payCode
     * @return
     */
    OrderPay validate(String payCode);
}