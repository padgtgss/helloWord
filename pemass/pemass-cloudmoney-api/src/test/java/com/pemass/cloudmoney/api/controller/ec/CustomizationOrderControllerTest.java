/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.ec;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: CustomizationOrderControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 18:05
 */
public class CustomizationOrderControllerTest {
    @Test
    public void testList() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("token",TestConst.TOKEN);
        param.put("uid",23);
        param.put("pageIndex", 1);
        param.put("pageSize", 10);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "customization/search", param);
        System.out.println(result);
    }
    @Test
    public void testdetailt() throws Exception {
        Map param = new HashMap();
        param.put("token","271e052e-9bac-4491-ae39-1e3d646db001");
        param.put("uid", 1);
        param.put("orderIdentifier", "102015011000228");
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "customization/order/detail", param);
        System.out.println(result);
    }
}
