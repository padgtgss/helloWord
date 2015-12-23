package com.pemass.cloudpos.api.controller.ec;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TicketControllerTest {

    @Test
    public void testTicket() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("token","ec501007a0174580a4bb638c8932cb43");
        param.put("terminalUserId","32");
        param.put("username","18284533637");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "ticket/3/checkin", param);
        System.out.println(result);
    }


    @Test
    public void showshow() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("token", TestConst.TOKEN_ROLE_TICKETER);
        param.put("terminalUserId", 2L);
//        param.put("username", "18284533637");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "ticket/084080351795/scan", param);
        System.out.println(result);
    }


    @Test
    public void showshow111() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("token", TestConst.TOKEN_ROLE_TICKETER);
        param.put("terminalUserId", 2L);
        param.put("pageSize", 1L);
        param.put("pageIndex", 20L);
//        param.put("username", "18284533637");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.TEST_CLOUDPOS_HTTP_V2 + "ticket/search", param);
//        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "ticket/search", param);
        System.out.println(result);
    }
}