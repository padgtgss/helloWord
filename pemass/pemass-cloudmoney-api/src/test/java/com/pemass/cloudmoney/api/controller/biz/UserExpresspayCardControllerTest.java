package com.pemass.cloudmoney.api.controller.biz;


import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserExpresspayCardControllerTest {

    @Test
      public void getuserExpresspayCard() throws  Exception{

        Map<String, Object> param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userExpresspayCard/user/1", param);
        System.out.println(result);
    }


    @Test
    public void binduserExpresspayCard() throws  Exception{

        Map<String, Object> param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", "1");
        param.put("cardNumber", "6200480131000209");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", "9999");
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userExpresspayCard/bind", param);
        System.out.println(result);
    }


    @Test
    public void unBinduserExpresspayCard() throws  Exception{

        Map<String, Object> param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("uid", "1");
        param.put("cardNumber", "2345678909875");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "userExpresspayCard/unbind", param);
        System.out.println(result);
    }
}