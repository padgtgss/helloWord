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

public class SiteControllerTest extends TestCase {

    @Test
    public void testGetSiteListByName() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        param.put("siteType", "-1");
//        param.put("cityId", 1L);
//        param.put("districtId", 3L);
//        param.put("distance", 100000);
        param.put("longitude", 104.055703);
        param.put("latitude", 30.549801);
        param.put("pageIndex", 1);
        param.put("pageSize", 100);


        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "site/search", param);
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "site/search", param);
        System.out.println(result);
    }

    public void testGetSiteListByName1() throws Exception {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("appid", TestConst.APP_ID);
        String stamp = UUID.randomUUID().toString();
            param.put("stamp", stamp );

            param.put("productId", 9);
            param.put("cityId", 235);
//            param.put("latitude", 104);
            param.put("pageIndex", 1);
            param.put("pageSize", 100);

            String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
            param.put("sign", MD5Util.encrypt(newSignString));
        System.out.println(MD5Util.encrypt(newSignString));
        System.out.println(stamp);
//            String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "site/search", param);
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "site/search", param);
//            System.out.println(result);
    }

    @Test
    public void testGetSiteInfoById() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "site/6", param);
        System.out.println(result);
    }
}