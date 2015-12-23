package com.pemass.cloudmoney.api.controller.sys;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigControllerTest {

    @Test
    public void testSave() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", "79f394dc713d4455a40a3d3d69814387");
        param.put("stamp", UUID.randomUUID().toString());
        param.put("configEnum", "DEPOSIR_POUNDAGE");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + "fe240884bb144cf5";
        param.put("sign","9999");

//        String result = HttpUtil.get("http://test.api.pemass.com/pemass-cloudmoney-api/v2/"+"config", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "config", param);
        System.out.println(result);
    }
}