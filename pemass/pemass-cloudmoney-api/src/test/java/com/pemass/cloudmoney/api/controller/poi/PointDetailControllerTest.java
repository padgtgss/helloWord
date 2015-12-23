package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointDetailControllerTest {


    /**
     *通用E积分，E通币 收支明细
     * @throws Exception
     */
    @Test
    public void testSelectDetail() throws Exception {
        Map param = new HashMap();
        param.put("pageSize", "10");
        param.put("pageIndex", "1");
        param.put("uid", "4");
        param.put("token","d415bb50c9774de3bfa9b6580800655e");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        //通用E积分
//       String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userPointDetail/p/general", param);
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "userPointDetail/p/general", param);

        //E通币

     //  String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userPointDetail/e/general", param);
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "userPointDetail/e/general", param);

        System.out.println(result);
    }


    /**
     * 定向积分按照积分发放方统计
     * @throws Exception
     */
    @Test
    public void testDirectionPointDetailByPool() throws Exception {
        Map param = new HashMap();
        param.put("pageSize", "5");
        param.put("pageIndex", "1");
        param.put("uid", "1");
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userPointDetail/p/direction/list", param);

        System.out.println(result);
    }

    /**
     * 定向积分收支明细
     * @throws Exception
     */
    @Test
    public void testDirectionPointDetail() throws Exception {
        Map param = new HashMap();
        param.put("pageSize", "10");
        param.put("pageIndex", "1");
        param.put("uid", "4");
        param.put("pointPoolId", "5");
        param.put("token", "d415bb50c9774de3bfa9b6580800655e");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "userPointDetail/p/direction", param);
//        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userPointDetail/p/direction", param);
        System.out.println(result);
    }



    /**
     * 定向积分可用商户
     * @throws Exception
     */
    @Test
    public void testDirectionPointOrg() throws Exception {
        Map param = new HashMap();
        param.put("pageSize", "10");
        param.put("pageIndex", "1");
        param.put("pointPoolId", "2");
        param.put("token","cd83281f8e61461e9a0e0f785e6b09c3");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "userPointDetail/p/direction/organization", param);

        System.out.println(result);
    }



    /**
     * E积分可用明细
     * @throws Exception
     */
    @Test
     public void testUserPointDetailP() throws Exception {
        Map param = new HashMap();
        param.put("pageSize", "5");
        param.put("pageIndex", "1");
        param.put("isDirection", true);
        param.put("uid", 1);
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userPointDetail/p", param);

        System.out.println(result);
    }


    /**
     * 通用E积分统计
     * @throws Exception
     */
    @Test
    public void testUserPointDetailGenP() throws Exception {
        Map param = new HashMap();
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userPointDetail/p/general/user/9", param);

        System.out.println(result);
    }



    /**
     * e通币统计
     * @throws Exception
     */
    @Test
    public void testUserPointDetailGenE() throws Exception {
        Map param = new HashMap();
        param.put("token",TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userPointDetail/e/user/5", param);

        System.out.println(result);
    }






}