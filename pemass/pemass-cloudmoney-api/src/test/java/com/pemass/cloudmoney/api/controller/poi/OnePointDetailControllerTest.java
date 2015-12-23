package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.*;

public class OnePointDetailControllerTest {


    /**
     * 当前用户赠送给朋友或是被朋友赠送的 未用积分余额总数
     * @throws Exception
     */
    @Test
    public void pointGiveDetailAmountCount() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",TestConst.TOKEN);
        map.put("appid", TestConst.APP_ID);
        map.put("uid", Long.valueOf(2));
        //积分为朋友赠送的
       // map.put("isGiven", true);
        //自己送给朋友的
       map.put("isGiven", false);
        map.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));

       String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointGiving/useableAmount", map);

        System.out.println(result);
    }


    /**
     * 当前用户被朋友赠送 或是 朋友赠送给当前用户  的剩余一元购积分 列表
     * @throws Exception
     */
    @Test
    public void pointGiveDetailAmountSearch() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",TestConst.TOKEN);
        map.put("appid", TestConst.APP_ID);
        map.put("pageIndex", "1");
        map.put("pageSize", "5");
        map.put("uid",Long.valueOf(1));
        //赠送给朋友
       // map.put("isGiven",false);
        //被朋友赠送
        map.put("isGiven",true);
        map.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointGiving/search", map);
        System.out.println(result);
    }


    /**V
     * 赠送壹购积分
     * @throws Exception
     */
    @Test
    public void pointGiveTest() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",TestConst.TOKEN);
        map.put("appid", TestConst.APP_ID);
        map.put("uid", "2");
        map.put("username", "1598204307");
        map.put("amount", "20");
        map.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointGiving", map);
        System.out.println(result);
    }


    /**
     * 壹购积分回收
     * @throws Exception
     */
    @Test
    public void callBackTest() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",TestConst.TOKEN);
        map.put("appid", TestConst.APP_ID);
        map.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointGiving/2/recycle", map);
        System.out.println(result);
    }


    /**
     * 积分赠送列表统计
     * @throws Exception
     */
    @Test
    public void pointDetailTest() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",TestConst.TOKEN);
        map.put("appid", TestConst.APP_ID);
        map.put("stamp", UUID.randomUUID().toString());
        map.put("pageIndex", "1");
        map.put("pageSize", "5");
        map.put("uid",Long.valueOf(1));
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "onePointGiving/useableAmount/detail", map);
        System.out.println(result);
    }


}