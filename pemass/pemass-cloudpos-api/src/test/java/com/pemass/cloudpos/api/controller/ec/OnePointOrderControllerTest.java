package com.pemass.cloudpos.api.controller.ec;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnePointOrderControllerTest extends TestCase {


    @Test
    public void testInsert() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", "82a84558ea0346f9ab9901a94ad0d383");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        param.put("username", "15982043073");
        param.put("terminalUserId", 60L);
        param.put("productId", 81L);
        param.put("amount", 1);
        param.put("totalPrice", 150);

        param.put("usePointE", 0);
        param.put("usePointO", 94);


        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post("http://dev.api.pemass.com/pemass-cloudpos-api/v2/" + "onepointorder/confirm", param);
        System.out.println(result);
    }

    public void testPay() throws Exception {

    }
}