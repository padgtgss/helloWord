package com.pemass.cloudmoney.api.controller.biz;


import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserExpresspayCardDetailControllerTest {



    @Test
    public void insertUserExpresspayCardDetail() throws  Exception {

        Map<String, Object> param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", "1");
        param.put("pointEAmount", "4");
        param.put("poundage", "23");
        param.put("money", "2323");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userExpresspayCardDetail/deposit", param);
        System.out.println(result);
    }


    @Test
    public void getuserExpresspayCardDetail() throws  Exception {

        Map<String, Object> param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", "11");
        param.put("pageIndex", "1");
        param.put("pageSize", "5");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userExpresspayCardDetail/search", param);
        System.out.println(result);
    }

    @Test
    public void getUserExpresspayCardDetailMoneyCout() throws  Exception {

        Map<String, Object> param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", "6");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userExpresspayCardDetail/money", param);
        System.out.println(result);
    }



}