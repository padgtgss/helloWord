package com.pemass.cloudmoney.api.controller.ec;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import common.TestConst;
import junit.framework.TestCase;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OnePointOrderControllerTest extends TestCase {


    @Test
    public void testInsert() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "efc5060e14d244bda14ad94cd764815e");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("username", "18683352528");
        param.put("siteId", 11L);
        param.put("productId", 22L);
        param.put("amount", 1);
        param.put("totalPrice", 1);

        param.put("usePointE", 0);
        param.put("usePointP", 0);
        param.put("usePointO", 0);
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderPointPayDetailPojo> orderPointPayDetailPojoList = new ArrayList<OrderPointPayDetailPojo>();
//        OrderPointPayDetailPojo orderPointPayDetailPojo = new OrderPointPayDetailPojo();
//        orderPointPayDetailPojo.setAmount(10);
//        orderPointPayDetailPojo.setBelongUserId(1L);
//        orderPointPayDetailPojo.setPointType(PointTypeEnum.P);
//        orderPointPayDetailPojo.setUserId(1L);
//        orderPointPayDetailPojoList.add(orderPointPayDetailPojo);
//        orderPointPayDetailPojo = new OrderPointPayDetailPojo();
//        orderPointPayDetailPojo.setAmount(10);
//        orderPointPayDetailPojo.setBelongUserId(1L);
//        orderPointPayDetailPojo.setPointType(PointTypeEnum.E);
//        orderPointPayDetailPojo.setUserId(1L);
//        orderPointPayDetailPojoList.add(orderPointPayDetailPojo);
//        orderPointPayDetailPojo = new OrderPointPayDetailPojo();
//        orderPointPayDetailPojo.setAmount(0);
//        orderPointPayDetailPojo.setBelongUserId(1L);
//        orderPointPayDetailPojo.setPointType(PointTypeEnum.O);
//        orderPointPayDetailPojo.setUserId(1L);
//        orderPointPayDetailPojoList.add(orderPointPayDetailPojo);
        String orderPointPayDetail = objectMapper.writeValueAsString(orderPointPayDetailPojoList);
        System.out.println(orderPointPayDetail);
        param.put("orderPointPayDetail", orderPointPayDetail);

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onepointorder", param);
//        String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "onepointorder", param);
        System.out.println(result);
    }
}