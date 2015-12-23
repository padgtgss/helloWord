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

public class LoadingControllerTest extends TestCase {

    @Test
    public void testLoadingimg() throws Exception {
        long startTime = System.currentTimeMillis();
        String password = "123456";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

//        String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "auth/login", param);
        String result = HttpUtil.get("http://test.api.pemass.com/pemass-cloudmoney-api/v2/loading", param);
        System.out.println(result);

        System.out.println(System.currentTimeMillis() - startTime);
    }
}