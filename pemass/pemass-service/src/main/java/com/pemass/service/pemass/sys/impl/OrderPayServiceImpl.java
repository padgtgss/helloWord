/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.sys.OrderPayDao;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.OrderPay;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.EcError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.sys.OrderPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: OrderPayServiceImpl
 * @Author: lin.shi
 * @CreateTime: 2015-09-22 10:58
 */
@Service
public class OrderPayServiceImpl implements OrderPayService {

    @Resource
    private OrderPayDao orderPayDao;

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public OrderPay getById(Long uid) {
        OrderPay orderPay = this.save(uid);
        return orderPayDao.getOrderPayById(orderPay.getPayCode());
    }

    @Override
    public OrderPay save(Long uid) {
        User user = jpaBaseDao.getEntityById(User.class,uid);
        if(user == null){
            throw  new BaseBusinessException(SysError.USER_NOT_EXIST);
        }
        OrderPay orderPay = new OrderPay();
        orderPay.setUserId(user.getId());
        orderPay.setUsername(user.getUsername());
        orderPay.setPayCode(UUIDUtil.randomNumber(18));
         orderPayDao.save(orderPay);
        return orderPay;
    }

    @Override
    public OrderPay validate(String payCode){
        OrderPay _orderPay = orderPayDao.getOrderPayById(payCode);
        if(_orderPay != null){
            orderPayDao.delOrderPay(_orderPay);
            return _orderPay;
        }else
            throw  new BaseBusinessException(EcError.PAY_CODE_EXPIRY);

    }


}