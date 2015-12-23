/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.sys.OrderCodeDao;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.redis.OrderCode;
import com.pemass.service.exception.EcError;
import com.pemass.service.pemass.sys.OrderCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: OrderServiceImpl
 * @Author: lin.shi
 * @CreateTime: 2015-09-28 10:40
 */
@Service
public class OrderCodeServiceImpl implements OrderCodeService {

    @Resource
    private OrderCodeDao orderCodeDao;

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public OrderCode get(String orderIdentifier) {
        OrderCode  orderCode = this.save(orderIdentifier);
        return orderCodeDao.get(orderCode.getCode());
    }

    @Override
    public OrderCode save(String orderIdentifier) {
        OrderCode order = new OrderCode();
        order.setOrderIdentifier(orderIdentifier);
        order.setCode(UUIDUtil.randomNumber(18));
        orderCodeDao.save(order);
        return order;
    }

    @Override
    public Order validate(String orderCode) {
        OrderCode _orderCode = orderCodeDao.get(orderCode);
        if(_orderCode != null){
            orderCodeDao.delOrder(_orderCode);
            return jpaBaseDao.getEntityByField(Order.class, "orderIdentifier", _orderCode.getOrderIdentifier());
        }else{
            throw new BaseBusinessException(EcError.ORDER_CODE_EXPIRY);
        }
    }

}