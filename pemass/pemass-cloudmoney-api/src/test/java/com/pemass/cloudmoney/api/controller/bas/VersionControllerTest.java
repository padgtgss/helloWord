package com.pemass.cloudmoney.api.controller.bas;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VersionControllerTest extends TestCase {

    @Test
    public void testCheck() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("buildNumber", "1");
        param.put("appid", TestConst.APP_ID);
        param.put("appType", "C");
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param));
        newSignString = newSignString + "&" + TestConst.KEY;
        System.out.println("MD5前原始数据：" + newSignString);
        System.out.println("MD5后的数据："+ MD5Util.encrypt(newSignString));

        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "version", param);
        System.out.println(result);

    }

    @Test
    public void testAudit() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param));
        newSignString = newSignString + "&" + TestConst.KEY;
        System.out.println("MD5前原始数据：" + newSignString);
        System.out.println("MD5后的数据："+ MD5Util.encrypt(newSignString));

        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "version/audit", param);
        System.out.println(result);

    }


}