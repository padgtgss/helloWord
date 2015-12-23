package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class OnePointConsumeDetailControllerTest {


    /**
     * 统计自己赚的跟朋友赚的 还款总数
     * @throws Exception
     */
    @Test
    public void testProfitCount() throws Exception {
        Map param = new HashMap();
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointConsumeDetail/profitCount/4", param);
        System.out.println(result);
    }


    /**
     * 还款额赚取详情
     * @throws Exception
     */
    @Test
    public void testProfitDetail() throws Exception {
        Map param = new HashMap();
         param.put("pageSize", "5");
         param.put("pageIndex", "1");
            param.put("isYouself", false);
      //  param.put("isYouself", true);
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointConsumeDetail/profit/1", param);
        System.out.println(result);
    }


    /**
     * 统计消费记录
     * @throws Exception
     */
    @Test
    public void testConsumeDetail() throws Exception {
        Map param = new HashMap();
        param.put("pageSize", "5");
        param.put("pageIndex", "1");
        param.put("uid", "2");
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointConsumeDetail/search", param);
        System.out.println(result);
    }


    /**
     * 帮某一位朋友花费的壹购积分详情
     * @throws Exception
     */
    @Test
    public void testForFriendDetail() throws Exception {
        Map param = new HashMap();
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("uid","5");
        param.put("pageIndex","1");
        param.put("pageSize","5");
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointConsumeDetail/friend/15208416578", param);
        System.out.println(result);
    }


}