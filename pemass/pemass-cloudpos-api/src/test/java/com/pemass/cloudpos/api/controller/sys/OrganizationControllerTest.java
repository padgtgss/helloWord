package com.pemass.cloudpos.api.controller.sys;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrganizationControllerTest {

    @Test
    public void testLists() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId","2");
        param.put("provinceId","1");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "organization/search", param);
        System.out.println(result);
    }
}