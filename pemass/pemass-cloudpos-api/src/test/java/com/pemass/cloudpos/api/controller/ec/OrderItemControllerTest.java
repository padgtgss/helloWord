package com.pemass.cloudpos.api.controller.ec;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.*;

public class OrderItemControllerTest {

    @Test
    public void testTicket() throws Exception {

        Map param = new HashMap();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId", 32);

        param.put("username", "18284533637");
        param.put("ticketCode", "780824");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post("http://localhost:8888/pemass-cloudpos-api/v2/orderItem/ticket", param);
        System.out.println(result);
    }
}