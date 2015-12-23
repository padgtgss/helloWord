package com.pemass.cloudpos.api.controller.bas;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ValidateControllerTest extends TestCase {

    public void testValidate4CustomizationOrder() throws Exception {

    }

    public void testSendMessage() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("smsType", "ORDER_VAL_REGED");
        param.put("telephone", "18683352528");
        param.put("totalPrice", 1);
        param.put("usePointE", 1);
        param.put("usePointP", 1);
        param.put("usePointO", 1);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTP_V2 + "validate", param);
        String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2 + "validate", param);
        System.out.println(result);
    }


    @Test
    public void test11() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("validateCode", "774003");

        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "validate/868", param);
//        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "validate/868", param);
        System.out.println(result);
    }
}