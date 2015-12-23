package com.pemass.cloudpos.api.controller.bas;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageControllerTest extends TestCase {

    @Test
    public void testSendMessage() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("username", "18008068200");
        param.put("totalPrice", "100");
        param.put("usePointE", "1");
        param.put("usePointP", "2");
        param.put("usePointO", "3");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTP_V2 + "message/order", param);
        System.out.println(result);
    }

    public void testSendMessage1() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "80433135e7404489ab90eeea3bdb03d1");
        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        param.put("username", "18284533637");
        param.put("totalPrice", "1");
        param.put("usePointE", "2");
        param.put("usePointP", "3");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post("http://localhost:8888/pemass-cloudpos-api/v2/message/order", param);
        System.out.println(result);
    }

    @Test
    public void testReturnMessage() throws Exception {
        String res = AESUtil.encrypt("jft770880","838e35fc9b784cbd");
//        String res = AESUtil.decrypt("CKscA1DoDl7HdmM2Se252A==", "6475ff6c37cf4751");
        System.out.println(res);

//        System.out.println(AESUtil.encrypt("jft770880", "CbIWnKl+uLlucx3r51FVuA=="));
    }



}