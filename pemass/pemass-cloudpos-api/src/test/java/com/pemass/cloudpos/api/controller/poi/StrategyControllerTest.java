package com.pemass.cloudpos.api.controller.poi;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrategyControllerTest extends TestCase {

    public void testSelectByTerminalUserId() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "2efddbfa6ae24a1286698139cfc9475b");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        param.put("terminalUserId", 26);



        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "strategy/search", param);
//        String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTPS_V2 + "order", param);
        System.out.println(result);
    }

    public void testDetail() throws Exception {

    }
}