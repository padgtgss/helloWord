package com.pemass.cloudpos.api.controller.biz;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.enumeration.CardVarietyEnum;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

public class ExpresspayCardControllerTest {

    @Test
    public void testAdd() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("cardNumber","6205150310000001,6205150310000002");
        param.put("cardCategory","CHIP_CARD,DISK_CARD");
        param.put("terminalUserId",82);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2+"expresspayCard", param);
        System.out.println(result);

    }
    @Test
    public void testAdds() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId",82);
        param.put("beginNumber","6205150310000003");
        param.put("endNumber","6205150310000100");
        param.put("cardVarietyEnum","DISK_CARD");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2+"expresspayCard", param);
        System.out.println(result);

    }
    @Test
    public void testAdds11() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId",82);

        param.put("cardNumber","6205150310000001,6205150310000002,6205150310000003,6205150310000004");
        param.put("organizationId",1);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2+"expresspayCard/allot", param);
        System.out.println(result);

    }
    @Test
    public void testAdds12() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId",82);
        param.put("beginNumber","6205150310000153");
        param.put("endNumber","6205150310000157");
        param.put("organizationId",1);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTP_V2+"expresspayCard/allot", param);
        System.out.println(result);

    }
    @Test
    public void testAdds14() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId", "82");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2+"expresspayCard/6205150310000001", param);
        System.out.println(result);

    }
     //库存管理 统计详情
    @Test
    public void testAdds114() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2+"expresspayCard/count/terminalUser/1", param);
        System.out.println(result);

    }

    //库存管理 查询待售卡详情
    @Test
    public void testAdds1114() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId", "82");
        param.put("pageIndex", "1");
        param.put("pageSize", "10");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2+"expresspayCard/onSaleCount/search", param);
        System.out.println(result);

    }   //库存管理 查询已售卡详情
    @Test
    public void testAdds11214() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId", "82");
        param.put("pageIndex", "1");
        param.put("pageSize", "10");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2+"expresspayCard/soldCount/search", param);
        System.out.println(result);

    }
}