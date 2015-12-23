package com.pemass.service.pemass.ec.impl;

import com.pemass.service.pemass.ec.OrderService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;


@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class OrderServiceImplTest extends AbstractTestNGSpringContextTests {


    @Resource
    private OrderService orderService;

    @Test
    public void testSelectOrderListByUid() {
        //orderService.insertCustomizationOrder("18010558729", 1000f, 1L, null, null, null);
    }

    @Test
    public void testpay() {
//        orderService.payCustomizationOrder(2L, PaymentTypeEnum.OFFLINE, 1L, null, null, null, deviceId);
    }

    @Test
    public void testGetHasPayOrderAmountOfDay() {
        Long amount = orderService.getHasPayOrderAmountOfDay();
        System.out.println("============已支付订单数量:" + amount);
    }
    @Test
    public void testGetHasPayOrderPricesOfDay() {
        Float amount = orderService.getHasPayOrderPricesOfDay();
        System.out.println("============已支付订单交易额:" + amount);
    }
    @Test
    public void testGetHasPayOrderOfDayByField() {
        Long amount = orderService.getHasPayOrderOfDayByField(2);
        System.out.println("============已支付订单数量:" + amount);
    }



}