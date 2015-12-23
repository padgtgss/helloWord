package com.pemass.cloudpos.api.controller.poi;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointDetailControllerTest extends TestCase {

    @Test
    public void testGetPoint() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("token",TestConst.TOKEN_ROLE_CASHIER);
        param.put("username", "234233");
//        param.put("productId", 13L);
        param.put("terminalUserId", 1L);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 +"pointDetail/query", param);
//        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "pointDetail/query", param);
        System.out.println(result);
    }
}