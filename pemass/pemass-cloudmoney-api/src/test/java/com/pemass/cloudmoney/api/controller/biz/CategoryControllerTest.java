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

public class CategoryControllerTest extends TestCase {


    @Test
    public void testGetCategoryList() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("token", "19780acdb958448092a3770681ab1fc3");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "productCategory/search", param);
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "productCategory/search", param);
        //http://pre.api.pemass.com/pemass-cloudmoney-api/v2
//        String result = HttpUtil.get("http://pre.api.pemass.com/pemass-cloudmoney-api/v2/" + "productCategory/search", param);
        System.out.println(result);
    }
}