package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LotteryDrawControllerTest {
    @Test
    public void testExecute() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token","fc632a45cdb543d185178cbca4ad420c");
        param.put("appid", TestConst.APP_ID);
        param.put("uid", "2");
        param.put("prize", "400");
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2+"lottery/draw", param);
        System.out.println(result);
    }
}