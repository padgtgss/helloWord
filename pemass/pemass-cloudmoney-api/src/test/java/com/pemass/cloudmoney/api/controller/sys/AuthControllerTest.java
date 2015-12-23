/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.sys;

import com.pemass.common.core.util.AESUtil;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: AuthControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2015-04-16 10:55
 */
public class AuthControllerTest {
    @Test
    public void testLogin() throws Exception {
        long startTime = System.currentTimeMillis();
        String password = "123456";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("username", "18008068201");
        param.put("stamp", UUID.randomUUID().toString());
        param.put("deviceId", 1);
        param.put("password", AESUtil.encrypt(password, TestConst.KEY));

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

       // String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTPS_V2 + "auth/login", param);
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "auth/login", param);
        System.out.println(result);

        System.out.println(System.currentTimeMillis() - startTime);
    }

    @Test
    public void testRegister() throws Exception {
        String password = "456789";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("username", "18008068200");
        param.put("stamp", UUID.randomUUID().toString());
        param.put("password", AESUtil.encrypt(password, TestConst.KEY));
        param.put("validateCode", "322415");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "auth/register", param);
        System.out.println(result);
    }

    @Test
    public void testForgetPassword() throws Exception {
        String password = "123456";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("username", "18284533637");
        param.put("stamp", UUID.randomUUID().toString());
        param.put("password", AESUtil.encrypt(password, TestConst.KEY));
        param.put("validateCode", "836342");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "auth/forget", param);
        System.out.println(result);
    }

    @Test
    public void testUpdatePassword() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("uid", "19");
        param.put("stamp", UUID.randomUUID().toString());
        param.put("originalPassword", AESUtil.encrypt("123456", TestConst.KEY));
        param.put("newPassword", AESUtil.encrypt("666666", TestConst.KEY));
        param.put("token", "5eb57127f48e43bba4d1bea90e92caf9");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "auth/password", param);
        System.out.println(result);
    }
}
