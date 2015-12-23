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

public class ProductControllerTest extends TestCase {

    @Test
    public void testGetProuducts() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "67be88895b284badb8e8950700c2db28");
//        param.put("productName","电源");
        param.put("productType", 1);
//        param.put("productCategoryId", 27L);
//        param.put("siteId", 1L);
        param.put("cityId", 36L);
//        param.put("districtId", 2223L);
        param.put("orerByFiledName", "outPrice");
        param.put("orderBy", "ASC");
        param.put("pageIndex", 1);
        param.put("pageSize", 100);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "product/search", param);
//        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "product/search", param);
        System.out.println(result);
    }

    @Test
    public void testGetProuductsBysiteId() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "67be88895b284badb8e8950700c2db28");

        param.put("siteId", 1L);
        param.put("pageIndex", 1);
        param.put("pageSize", 10);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));


        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "product/search", param);
        System.out.println(result);
    }


    @Test
    public void testGetProuductById() throws Exception {
        long startTime = System.currentTimeMillis();

        Map<String, Object> param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "product/1", param);
//        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "product/1", param);
        System.out.println(result);

        System.out.println(System.currentTimeMillis() - startTime);

    }
}