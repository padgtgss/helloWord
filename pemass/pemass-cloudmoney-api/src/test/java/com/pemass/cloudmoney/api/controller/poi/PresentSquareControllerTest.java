package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PresentSquareControllerTest {

    //查询商家发放到 红包广场的所有策略类型红包
    @Test
    public void selectOrganization() throws Exception {
        Map param = new HashMap();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", 1L);
        param.put("pageSize",10);
        param.put("pageIndex",1);
        param.put("token", TestConst.TOKEN);

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param));
        newSignString = newSignString + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "presentSquare/search", param);
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "presentSquare/search", param);
        System.out.println(result);
    }


   //根据红包广场策略id,查询明细
    @Test
    public void selectOrganization1() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", 2);
        param.put("issueStrategyId",2);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "presentSquare/present/search", param);
        System.out.println(result);
    }

    //用户抢红包操作
    @Test
    public void testuserSendPresent() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("issueStrategyId",81L);
        param.put("uid",21L);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "presentSquare/grab", param);
        System.out.println(result);
    }

    @Test
    public void testGrabPresent() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("issueStrategyId",88L);
        param.put("uid",21L);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "presentSquare/grab/unpack", param);
        System.out.println(result);
    }



}