package com.pemass.cloudmoney.api.controller.ec;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TicketControllerTest extends TestCase {

    @Test
    public void testTicket() throws Exception {
        Map param = new HashMap();
        param.put("token", TestConst.TOKEN);

        param.put("uid", 21L);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("pageIndex", 1);
        param.put("pageSize", 100);
        param.put("useStatus", "UNUSED");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "ticket/search", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "ticket/search", param);
        System.out.println(result);
    }

    public void testDetail() throws Exception {
    }

    public void testOverdue() throws Exception {

    }

    public void testShare() throws Exception {
        Map param = new HashMap();
        param.put("token", TestConst.TOKEN);

        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "ticket/search", param);
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "ticket/share/24", param);
        System.out.println(result);
    }
}