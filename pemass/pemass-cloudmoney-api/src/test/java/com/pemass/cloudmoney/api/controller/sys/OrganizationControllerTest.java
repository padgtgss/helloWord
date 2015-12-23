package com.pemass.cloudmoney.api.controller.sys;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrganizationControllerTest {

    @Test
    public void testSave() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("accountname", "huodong@pemass3.com");
        param.put("password", "123456");
        param.put("organizationName", "本公司");
        param.put("business", "卖冰糕");
        param.put("license", "/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        param.put("taxCertificate", "/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        param.put("organizationCertificate", "/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        param.put("licenseNumber", "111");
        param.put("taxCertificateNumber", "111");
        param.put("organizationCertificateNumber", "111");
        param.put("legalName", "标题");
        param.put("legalIdcard", "333");
        param.put("legalIdcardUrl", "/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        param.put("legalIdcardUrlBack", "/resources/201502/05/2271ac0ac9c94aecabaf2cb0a4f6d3a4.jpeg");
        param.put("location", "标题");
        param.put("province.id", "1");
        param.put("city.id", "1");
        param.put("district.id", "1");

        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2+"organization" , param);
        System.out.println(result);

    }
}