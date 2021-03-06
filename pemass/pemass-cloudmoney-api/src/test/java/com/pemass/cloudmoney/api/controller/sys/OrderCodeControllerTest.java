package com.pemass.cloudmoney.api.controller.sys;


import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderCodeControllerTest {



    @Test
    public void  TestGet()throws  Exception{

        Map<String, Object> map = new HashMap();
        map.put("appid", TestConst.APP_ID);
        map.put("stamp", UUID.randomUUID().toString());
        map.put("token", TestConst.TOKEN);
        map.put("orderIdentifier", "2345678905");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "orderCode", map);
        System.out.println(result);

    }

}