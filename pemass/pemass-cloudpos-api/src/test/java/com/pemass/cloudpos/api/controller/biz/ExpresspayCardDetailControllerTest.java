package com.pemass.cloudpos.api.controller.biz;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.*;

public class ExpresspayCardDetailControllerTest {

    @Test
    public void testAllotSearch() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId",82);
        param.put("pageIndex","1");
        param.put("pageSize","4");
        //   param.put("token","999999");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token", "5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2+"expresspayCard/detail/inTransit/search", param);
        System.out.println(result);
    }
    @Test
    public void testAdds17() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId",82);
        param.put("pageIndex","1");
        param.put("pageSize","4");
        //   param.put("token","999999");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2+"/expresspayCard/detail/putaway/search", param);
        System.out.println(result);

    }

    @Test
    public void testAdds16() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId",1);
        param.put("pageIndex","1");
        param.put("pageSize","4");
        //   param.put("token","999999");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2+"/expresspayCard/detail/duein/search", param);
        System.out.println(result);

    }
    @Test
      public void testAdds18() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId",1);
        param.put("pageIndex","1");
        param.put("pageSize","4");
        //   param.put("token","999999");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.get(TestConst.DEV_CLOUDPOS_HTTP_V2+"/expresspayCard/detail/received/search", param);
        System.out.println(result);

    }
    @Test
    public void testAdds19() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("terminalUserId",1);
        param.put("cardIdntifier","14201506100945");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
        param.put("token","5dd5e574ac4048a2a81208516726caa6");
        String result = HttpUtil.post(TestConst.DEV_CLOUDPOS_HTTP_V2+"/expresspayCard/detail/putaway", param);
        System.out.println(result);

    }
}