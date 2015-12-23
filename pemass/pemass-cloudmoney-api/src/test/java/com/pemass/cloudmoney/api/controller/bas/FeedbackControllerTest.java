/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.bas;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: FeedbackControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2015-04-14 17:56
 */
public class FeedbackControllerTest {


    @Test
    public void testInsert() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("targetId", "1");
        param.put("appid", TestConst.APP_ID);
        param.put("feedbackType", "C");
        param.put("content", "测试内容哇哈哈");


        param.put("stamp", UUID.randomUUID().toString());


        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param));
        newSignString = newSignString + "&" + TestConst.KEY;
        System.out.println("MD5前原始数据：" + newSignString);
        System.out.println("MD5后的数据："+ MD5Util.encrypt(newSignString));

        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "feedback", param);
        System.out.println(result);
    }
}
