package com.pemass.cloudpos.api.controller.biz;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductControllerTest {

    @Test
    public void search() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token",TestConst.TOKEN_ROLE_CASHIER);
        param.put("terminalUserId", 1L);
//        param.put("productName", "电源");
        param.put("productType", 2);
//        param.put("productCategoryId", 27L);
//        param.put("siteId", 1L);
//        param.put("cityId", 235L);
//        param.put("districtId", 2223L);
        param.put("orerByFiledName", "outPrice");
        param.put("orderBy", "ASC");
        param.put("pageIndex", 1);
        param.put("pageSize", 100);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

//        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "product/search", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "product/search", param);
        System.out.println(result);
    }


    @Test
    public void getinfo() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "product/1", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "product/1", param);
        System.out.println(result);
    }
}