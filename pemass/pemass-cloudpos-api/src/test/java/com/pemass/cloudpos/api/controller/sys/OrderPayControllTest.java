package com.pemass.cloudpos.api.controller.sys;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import com.pemass.service.constant.PemassConst;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class OrderPayControllTest {

    @Test
    public void testCheck() throws Exception {

        Map<String, Object> map = new HashMap();
        map.put("appid", TestConst.APP_ID);
        String payCode ="004319753777671535";
        map.put("payCode", payCode);
        map.put("stamp", UUID.randomUUID().toString());
        map.put("token", TestConst.TOKEN_ROLE_CASHIER);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "orderPay/validate", map);
        System.out.println(result);


    }
}