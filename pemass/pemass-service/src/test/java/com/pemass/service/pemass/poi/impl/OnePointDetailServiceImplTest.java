package com.pemass.service.pemass.poi.impl;

import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import com.pemass.service.pemass.poi.OnePointDetailService;
import common.AbstractPemassServiceTest;

import org.testng.annotations.Test;

import javax.annotation.Resource;

public class OnePointDetailServiceImplTest extends AbstractPemassServiceTest {

    @Resource
    private OnePointDetailService onePointDetailService ;
    @Test
    public void testPointDeduct() throws Exception {

        OrderPointPayDetailPojo orderPointPayDetailPojo = new OrderPointPayDetailPojo();
        orderPointPayDetailPojo.setUserId(1L);
        orderPointPayDetailPojo.setOrderId(2L);
        orderPointPayDetailPojo.setAmount(900);
        orderPointPayDetailPojo.setBelongUserId(2L);
        onePointDetailService.pointDeduct(orderPointPayDetailPojo);

    }
}