package com.pemass.cloudmoney.api.controller.biz;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewsControllerTest extends TestCase {

    @Test
    public void testGetNewslist() throws Exception {
        Map param = new HashMap();
        param.put("newsType", "COMMUNICATION");
        param.put("pageIndex", 1L);
        param.put("pageSize", 11L);
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "news/NewsList", param);
        System.out.println(result);
    }


    @Test
    public void testGetNewsInfo() throws Exception {
        Map param = new HashMap();
//        param.put("productId", 11L);
        param.put("newsId", 29L);
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "news/NewsInfo", param);
        System.out.println(result);
    }


    @Test
    public void testGetNewslistv2() throws Exception {
        Map param = new HashMap();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        param.put("newsType", "COMMUNICATION");
        param.put("pageIndex", 1L);
        param.put("pageSize", 1000L);

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));



//        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "news/search", param);
        //
//        String result = HttpUtil.get("http://dev.api.pemass.com/pemass-cloudmoney-api/v2/" + "news/search", param);
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "news/search", param);
        System.out.println(result);
    }

    @Test
    public void testGetNewsInfov2() throws Exception {
        Map param = new HashMap();
//        param.put("productId", 11L);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "news/26", param);
        System.out.println(result);
    }
}