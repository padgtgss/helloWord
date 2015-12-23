package com.pemass.cloudmoney.api.controller.bas;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocationControllerTest extends TestCase {



    @Test
    public void testGetList() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "67be88895b284badb8e8950700c2db28");
        param.put("cityName", "福州");

        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "location/search", param);
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "location/search", param);
        System.out.println(result);
    }


    @Test
    public void testGetList1() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "67be88895b284badb8e8950700c2db28");
        param.put("cityId", 1L);

        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "location/district", param);
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "location/search", param);
        System.out.println(result);
    }


    @Test
    public void testGetList2() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "67be88895b284badb8e8950700c2db28");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "location/district", param);
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "location/search", param);
        System.out.println(result);
    }


    @Test
    public void testGetList3() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "67be88895b284badb8e8950700c2db28");
        param.put("provinceId", 1L);

        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "location/city", param);
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "location/search", param);
        System.out.println(result);
    }
}