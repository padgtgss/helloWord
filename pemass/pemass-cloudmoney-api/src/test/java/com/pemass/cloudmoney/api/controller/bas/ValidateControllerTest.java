/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.bas;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: ValidateControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2015-04-16 10:51
 */
public class ValidateControllerTest {
    @Test
    public void testValidate4Register() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("telephone", "18602887140");
        param.put("smsType", "REG_VAL");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "validate", param);
//        String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "validate", param);
        System.out.println(result);
    }

    @Test
    public void testValidate4FindPassword() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("telephone", "18010558729");
        param.put("smsType", "FIND_PWD");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

//        String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTPS_V2 + "validate", param);
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "validate", param);
        System.out.println(result);
    }
    @Test
    public void testValidate4FindPassword1() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("telephone", "18284533637");
        param.put("key", "ORDER_VAL_NOREG");
        param.put("arg", "1");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "validate/testValidate4FindPassword1", param);
        System.out.println(result);
    }
}
