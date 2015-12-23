package com.pemass.cloudpos.api.controller.sys;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FeedbackControllerTest extends TestCase {
    @Test
    public void testInsert() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);

        param.put("stamp", UUID.randomUUID().toString());
        param.put("targetId", 1L);
        param.put("feedbackType", "C");
        param.put("content", "test");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

//        String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2 + "feedback", param);
        String result = HttpUtil.post(TestConst.TEST_CLOUDPOS_HTTP_V2 + "feedback", param);
        System.out.println(result);
    }
}