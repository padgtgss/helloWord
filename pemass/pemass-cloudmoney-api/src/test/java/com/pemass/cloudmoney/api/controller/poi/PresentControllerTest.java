package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PresentControllerTest {

    @Test
    public void testSearch() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "d415bb50c9774de3bfa9b6580800655e");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", "4");
//        param.put("presentStatus", PresentStatusEnum.HAS_RECEIVE);
        param.put("pageIndex", "1");
        param.put("pageSize", "10");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "present/search", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "present/search", param);
        System.out.println(result);
    }

    @Test
    public void testDetail() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "present/47", param);
        System.out.println(result);
    }

    @Test
    public void testUnpack() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "present/2/unpack", param);
        System.out.println(result);
    }


    @Test
    public void grant() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "present/720/grant", param);
        System.out.println(result);
    }




    @Test
    public void testGrant() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "1413142317ef43fe98816ebd179badae");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("username", "18008068200");
        param.put("uid", "1");
        param.put("presentName", "大红包哦");
        param.put("amount", "20");
        param.put("isGeneral", "1");
        param.put("instruction", "红包无说明");
        param.put("orderTicketId", "");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "present/pack/grant", param);
        System.out.println(result);
    }


    @Test
    public void testUnpack1() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
//        param.put("uid",7L);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "present/720/unpack", param);
        System.out.println(result);
    }
}