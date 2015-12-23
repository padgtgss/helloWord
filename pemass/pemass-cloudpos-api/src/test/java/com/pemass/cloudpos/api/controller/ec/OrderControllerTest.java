package com.pemass.cloudpos.api.controller.ec;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import junit.framework.TestCase;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderControllerTest extends TestCase {

    /**
     * 商品订单
     *
     * @throws Exception
     */
    @Test
    public void testInsertProductOrder() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("orderType", "PRODUCT_ORDER");
        param.put("username", "15982043073");
        param.put("terminalUserId", 1);
        param.put("productId", 1);
        param.put("amount", 1);
        param.put("totalPrice", 150);

        param.put("usePointE", 0);
        param.put("usePointP", 0);
        param.put("usePointO", 0);
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderPointPayDetailPojo> orderPointPayDetailPojoList = new ArrayList<OrderPointPayDetailPojo>();
        String orderPointPayDetail = objectMapper.writeValueAsString(orderPointPayDetailPojoList);
//        System.out.println(orderPointPayDetail);
        param.put("orderPointPayDetail", orderPointPayDetail);

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTP_V2 + "order/confirm", param);
        String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTPS_V2 + "order", param);
        System.out.println(result);



    }

    /**
     * 自定义订单
     *
     * @throws Exception
     */
    @Test
    public void testInsertCustomizationOrder() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        param.put("username", "18010558729");
        param.put("terminalUserId", 3L);
        param.put("originalPrice", 100);
        param.put("orderType", "CUSTOMIZATION_ORDER");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
       // String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTP_V2 + "order", param);
       String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2 + "order", param);
        System.out.println(result);
    }

    @Test
    public void testPay() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
//        @PathVariable("orderId") Long orderId, PaymentTypeEnum paymentType,
//                Long terminalUserId, String externalPaymentIdentifier, String payId, Integer deviceId, String
//        posSerial, Integer isSucceed
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("paymentType", "OFFLINE");
//        param.put("validateCode", "800900");
        param.put("terminalUserId", 1L);
        param.put("externalPaymentIdentifier", "1234567890");
        param.put("payId", "2202497");
        param.put("deviceId", 1);
        param.put("isSucceed ", 1);//posSerial
        param.put("posSerial ", "12333");//
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());


        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));


//        String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTPS_V2 + "order/868/pay", param);
        String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2 + "order/14/pay", param);
//        String result = HttpUtil.post("https://pre.api.pemass.com:8443/pemass-cloudmoney-api/v2/" + "order/14/pay", param);
        System.out.println(result);
    }

    public void testList() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "ecf19e486cc34401a98d8364377b4c37");
        param.put("appid", "e58ca287c68940d98c541ee90bddc536");
        param.put("stamp", UUID.randomUUID().toString());

        param.put("username", "18008068211");
        param.put("terminalUserId", 26L);
        param.put("orderStatusEnum", OrderStatusEnum.CONFIRMED);
        param.put("orderTypeEnum", OrderTypeEnum.PRODUCT_ORDER);
        param.put("payStatusEnum", PayStatusEnum.NONE_PAY);
        param.put("pageIndex", 1);
        param.put("pageSize", 10);

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + "da24aed9bc8e4744";
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "order/search", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "order/search", param);
        System.out.println(result);
    }

    public void testGetOrderInfo() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "order/mail", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "order/mail", param);
        System.out.println(result);
    }

    public void testGetOrderInfoById() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId", 1L);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "order/identifier/8010001964", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "order/identifier/8000001041", param);
        System.out.println(result);
    }

    public void testReturnOrder() throws Exception {
        System.out.println(new DateTime(new Date()).toString("yyyy-MM-dd"));

    }

    public void testSumOrder() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());


        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "order/search", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "order/terminaluser/1/statistics", param);
        System.out.println(result);
    }
}