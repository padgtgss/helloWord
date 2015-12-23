package com.pemass.cloudpos.api.controller.sys;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthControllerTest {

    @Test
    public void testTerminalLogin() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        param.put("terminalUsername", "jianp");
        param.put("password", AESUtil.encrypt("123456", TestConst.KEY));
        param.put("deviceId", 1L);

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTP_V2 + "auth/login", param);
        System.out.println(result);
    }

    @Test
    public void testUpdatePassword() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);

        param.put("terminalUserId", "5");
        param.put("newPassword", AESUtil.encrypt("123456", TestConst.KEY));
        param.put("originalPassword", AESUtil.encrypt("111111", TestConst.KEY));

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2 + "auth/password", param);
        System.out.println(result);
    }
}