package com.pemass.cloudmoney.api.controller.sys;


import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreditRecordControllerTest {


    @Test
    public void applyCreditTest() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("token", "b2dad91188f64e509dc0a52de090eea2");
        param.put("stamp", UUID.randomUUID().toString());
        param.put("userId", "9");
        param.put("name", "malimali哄到底");
        param.put("telephone", "87654");
        param.put("bankAccount", "23456789");
        param.put("officeLocation", "四川");
        param.put("location", "成都");
        param.put("legalIdcard", "5114567865433546645");


        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "creditRecord");

        FileBody legalIdcardFile = new FileBody(new File("C:\\Users\\Mr.Shi\\Desktop\\11.png"), "image");
        FileBody legalIdcardBackFile = new FileBody(new File("C:\\Users\\Mr.Shi\\Desktop\\11.png"), "image");
        FileBody faceFile = new FileBody(new File("C:\\Users\\Mr.Shi\\Desktop\\11.png"), "image");

        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        System.out.println(newSignString);
        reqEntity.addPart("sign", new StringBody(MD5Util.encrypt(newSignString)));

        for (Map.Entry entry : param.entrySet()) {
            reqEntity.addPart(entry.getKey().toString(), new StringBody(entry.getValue().toString(), Charset.forName("utf-8")));
        }

        reqEntity.addPart("legalIdcardFile", legalIdcardFile);
        reqEntity.addPart("legalIdcardBackFile", legalIdcardBackFile);
        reqEntity.addPart("faceFile", faceFile);

        httppost.setEntity(reqEntity);
        httppost.setHeader("token", "b2dad91188f64e509dc0a52de090eea2");

        HttpResponse response = httpclient.execute(httppost);

        HttpEntity resEntity = response.getEntity();
        System.out.println(EntityUtils.toString(resEntity));


    }


    /**
     * 授信详情查询
     */
    @Test
    public void TestUseableCreditLimit() throws Exception{

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appid", TestConst.APP_ID);
        param.put("token", "b2dad91188f64e509dc0a52de090eea2");
        param.put("stamp", UUID.randomUUID().toString());
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));

        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "creditRecord/user/6", param);
        System.out.println(result);
    }

}