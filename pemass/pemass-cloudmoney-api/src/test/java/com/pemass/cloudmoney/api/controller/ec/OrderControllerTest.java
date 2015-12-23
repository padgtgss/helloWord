/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.ec;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.QRCodeUtil;
import com.pemass.common.core.util.SignatureUtil;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import common.TestConst;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: OrderControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 10:33
 */
public class OrderControllerTest {

    @Resource
    private ObjectMapper jacksonObjectMapper;

    @Test
    public void testList() throws Exception {
        long start = System.currentTimeMillis();
        Map<String,Object> param = new HashMap<String,Object>();
//        Long uid, OrderStatusEnum orderStatusEnum, OrderTypeEnum orderTypeEnum, PayStatusEnum payStatusEnum, Long
//        pageIndex, Long pageSize
        param.put("token", TestConst.TOKEN);
        param.put("uid", 1L);
//        param.put("orderStatusEnum", OrderStatusEnum.CONFIRMED);

        //Long uid,OrderStatusEnum orderStatusEnum , OrderTypeEnum orderTypeEnum,
        // PayStatusEnum payStatusEnum, Long pageIndex, Long pageSize
        param.put("orderTypeEnum", OrderTypeEnum.CUSTOMIZATION_ORDER);
        param.put("payStatusEnum", PayStatusEnum.NONE_PAY.toString());
        param.put("pageIndex", 1L);
        param.put("pageSize", 10L);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order/search", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order/search", param);
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testDetail() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
//        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "order/750", param);
        System.out.println(result);
    }

    @Test
    public void testTicket() {
        Map param = new HashMap();
        param.put("token", TestConst.APP_ID);
        param.put("uid", "1");
        param.put("pageIndex", 1);
        param.put("pageSize", 100);
        param.put("useStatus", OrderItemStatusEnum.UNUSED);

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order/item", param);
        System.out.println(result);
    }

    @Test
    public void test222() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("orderId", 828L);


        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order", param);
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order/cancel", param);
        System.out.println(result);
    }



    @Test
    public void testcode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        QRCodeUtil.encodeQRCode("pemass://color-run?64", baos, "png");
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        File file = new File("1.png");
        inputstreamtofile(bais,file);
        try {
            if (file == null || file.length() == 0) {
            }
            String filename = UUIDUtil.randomWithoutBar();
            File mediaFile = new File("E://广西彩色跑测试2.png" );
            FileUtils.copyFile(file, mediaFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }


    @Test
    public void testorder() throws IOException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("username", "18008068200");
        param.put("siteId", 1);
        param.put("productId", 1);
        param.put("amount", 1);
        param.put("totalPrice", 30);

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
        String orderPointPayDetail = objectMapper.writeValueAsString(orderPointPayDetailPojoList) ;
        System.out.println(orderPointPayDetail);
        param.put("orderPointPayDetail", orderPointPayDetail);

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order", param);
//        String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "order", param);
        System.out.println(result);

    }


    @Test
    public void nonpay() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", 15L);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order/obligations", param);
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "order/obligations", param);
        System.out.println(result);
    }

}
