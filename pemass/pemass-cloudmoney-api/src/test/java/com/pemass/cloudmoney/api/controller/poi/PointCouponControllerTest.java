/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: PointCouponControllerTest
 * @Author: lin.shi
 * @CreateTime: 2015-06-02 15:42
 */
public class PointCouponControllerTest {
    @Test
    public void testRechange() throws  Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",TestConst.TOKEN);
        map.put("appid",TestConst.APP_ID);
        map.put("packIdentifier","123456");
        map.put("stamp", UUID.randomUUID().toString());
        map.put("cardSecret","123456");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));
      //  String result = HttpUtil.post("http://dev.pemass.com/pemass-cloudmoney-api/v2//pointCoupon" , map);
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 +"/pointCoupon",map);
        System.out.println(result);
    }
}