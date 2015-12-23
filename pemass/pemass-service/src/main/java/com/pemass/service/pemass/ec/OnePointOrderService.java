/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec;

import com.pemass.persist.domain.jpa.ec.Order;

/**
 * @Description: OnePointOrderService
 * @Author: pokl.huang
 * @CreateTime: 2015-06-16 10:09
 */
public interface OnePointOrderService {

    Long insertC(String username, Long siteId, Long productId, Integer amount, Float totalPrice, Integer usePointE, Integer usePointO);

    Order getOrderById(Long orderId);

    Long insert(String username, Long terminalUserId, Long productId, Integer amount, Float totalPrice, Integer usePointE, Integer usePointO);

    Long updatePay(String username, Long orderId, Long productId, Integer amount, Long terminalUserId, Integer usePointE, Integer usePointP);
}
