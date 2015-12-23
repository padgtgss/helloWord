package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WalletControllerTest extends TestCase {

    /**
     * 积分统计
     * @throws Exception
     */
    @Test
    public void testSelectIntegral() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token",TestConst.TOKEN);

        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "wallet/1", param);
        System.out.println(result);
    }


    @Test
    public void testuseablePointCount() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("siteId", "11");
        param.put("productId", "12");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "wallet/useablePoint/1", param);
        System.out.println(result);
    }


    @Test
    public void testRepayment() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("uid", 2);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "wallet/repayment", param);
        System.out.println(result);
    }

}