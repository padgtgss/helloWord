package com.pemass.cloudpos.api.controller.poi;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CollectMoneyStategyControllerTest {

    @Test
    public void testList() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("terminalUserId", 6);
        param.put("token","a407e260-66b3-46ed-834b-0f4f8eb52dfe");
        String result = HttpUtil.get("http://dev.pemass.com/pemass-cloudpos-api/v1/collect_money_stategy/list", param);
        System.out.println(result);
    }
    @Test
    public void testdetail() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 +"stategy/2", param);
        System.out.println(result);
    }
    @Test
    public void computeDetail() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("terminalUserId", 1);
        param.put("collectMoneyStrategyId", 6);
        param.put("username","18284533637");
        param.put("consumptionAmount", 600.7);
        param.put("token","271e052e-9bac-4491-ae39-1e3d646db002");
        String result = HttpUtil.get("http://localhost/pemass-cloudpos-api/v1/collect_money_stategy/computeDetail", param);
        System.out.println(result);
    }


    @Test
    public void testGetPoint() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("appid", TestConst.POS_APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("token", TestConst.TOKEN_ROLE_CASHIER);

        param.put("terminalUserId", 12);

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.POS_KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2 + "stategy/search", param);
        System.out.println(result);
    }
}