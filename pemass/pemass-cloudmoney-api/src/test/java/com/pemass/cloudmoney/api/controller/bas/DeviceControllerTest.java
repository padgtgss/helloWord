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

public class DeviceControllerTest extends TestCase {

    @Test
    public void testAdd() throws Exception {


        Map<String, Object> param = new HashMap<String, Object>();
        param.put("deviceSerial", "123131");
        param.put("appid", "79f394dc713d4455a40a3d3d69814387");
        param.put("deviceModel", "iPhone OS啊啊啊");
        param.put("deviceName", "iPhone");
        param.put("deviceType", "IOS");
        param.put("resolution", "640x960");
        param.put("stamp", "DLRHWVRFPNADQKAEWLXCHBHIUSOQCZVHHPBN");
        param.put("systemVersion", "8.2");


        param.put("stamp", UUID.randomUUID().toString());


        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param));
        newSignString = newSignString + "&" + "fe240884bb144cf5";
        System.out.println("MD5前原始数据：" + newSignString);
        System.out.println("MD5后的数据："+MD5Util.encrypt(newSignString));

        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.PREPARE_CLOUDMONEY_HTTP_V2 + "device", param);
        System.out.println(result);

    }

}