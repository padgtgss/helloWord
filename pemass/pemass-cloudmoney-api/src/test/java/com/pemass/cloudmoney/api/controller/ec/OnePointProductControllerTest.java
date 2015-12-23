package com.pemass.cloudmoney.api.controller.ec;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnePointProductControllerTest extends TestCase {

    @Test
    public void testGetProuducts() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("token", TestConst.TOKEN);

        param.put("pageIndex", 1);
        param.put("pageSize", 10);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));


        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "onepointproduct/search", param);
        System.out.println(result);
    }
}