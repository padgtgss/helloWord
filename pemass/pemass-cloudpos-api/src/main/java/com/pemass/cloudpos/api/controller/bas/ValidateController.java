/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.bas;

import com.google.common.collect.ImmutableMap;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.ec.OrderService;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: 验证码
 * @Author: estn.zuo
 * @CreateTime: 2015-04-16 10:32
 */
@Controller
@RequestMapping("/validate")
public class ValidateController {

    @Resource
    public UserService userService;

    @Resource
    private SmsMessageService smsMessageService;


    @Resource
    private OrderService orderService;

    /**
     * 自定义收款时，发送验证码
     *
     * @param telephone
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "smsType=CUSTOM_ORDER_REGED")
    @ResponseBody
    public Object validate4CustomizationOrder(String telephone, Float totalPrice, Integer usePointE, Integer usePointP, Integer usePointO) {
        Object[] params = {totalPrice, usePointP, usePointO , usePointE};

        smsMessageService.append(telephone, SmsTypeEnum.CUSTOM_ORDER_REGED, params);
        return ImmutableMap.of("result", true);
    }

    /**
     * 订单
     * 发送短信
     *
     * @param telephone 手机号码
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(method = RequestMethod.POST, params = "smsType=ORDER_VAL_REGED")
    @ResponseBody
    public Object sendMessage(String telephone, Float totalPrice, Integer usePointE, Integer usePointP, Integer usePointO) {
        Object[] params = {totalPrice, usePointP, usePointO, usePointE};

        smsMessageService.append(telephone, SmsTypeEnum.ORDER_VAL_REGED, params);
        return ImmutableMap.of("result", true);
    }

    /**
     * 订单.支付验证码
     * <p/>
     * 包括商品订单和自定义订单
     *
     * @param orderId        订单ID
     * @param validateCode   验证码
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public Object pay(@PathVariable("orderId") Long orderId,String validateCode) {

        Order order = orderService.getById(orderId);
        User user = userService.getById(order.getUserId());

        Boolean flag = null;
        OrderTypeEnum orderType = order.getOrderType();
        if (OrderTypeEnum.PRODUCT_ORDER == orderType || OrderTypeEnum.ONE_PURCHASE_ORDER == orderType) {
            flag = smsMessageService.validateCode(user.getUsername(), SmsTypeEnum.ORDER_VAL_REGED, validateCode);

        }
        if (OrderTypeEnum.CUSTOMIZATION_ORDER == orderType) {
            flag =  smsMessageService.validateCode(user.getUsername(), SmsTypeEnum.CUSTOM_ORDER_REGED, validateCode);

        }


        return ImmutableMap.of("result",flag);
    }

}
