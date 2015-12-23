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
 * @Description: SmsControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2015-04-15 19:10
 */
public class SmsControllerTest {
    @Test
    public void testAdd() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("telephone", "18010558729");
        param.put("smsType","REG_VAL");
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param));
        newSignString = newSignString + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "validate", param);
        System.out.println(result);

    }
}
