/**
 * Copyright  2014  Pemass
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

/**
 * @Description: MessageControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2014-12-23 10:03
 */
public class MessageControllerTest {

    @Test
    public void testDetail() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "message/55c346e00cf2a20449cab61e", param);
        System.out.println(result);
    }

    @Test
    public void testSearch() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("uid", 6);
        param.put("appid", TestConst.APP_ID);
        param.put("pageIndex", 1);
        param.put("pageSize", 10);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "message/search", param);
        System.out.println(result);
    }

    @Test
    public void testDeleteAll() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("uid", 6);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "message/delete", param);
        System.out.println(result);
    }
}
