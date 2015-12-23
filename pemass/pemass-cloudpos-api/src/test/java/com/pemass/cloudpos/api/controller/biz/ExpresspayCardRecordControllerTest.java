package com.pemass.cloudpos.api.controller.biz;

import com.pemass.cloudpos.api.controller.TestConst;
import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.*;

public class ExpresspayCardRecordControllerTest {

    @Test
    public void testAdd() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appid", "e58ca287c68940d98c541ee90bddc536");
        param.put("stamp", UUID.randomUUID().toString());
        param.put("cardNumber","6205150310000022");
        param.put("serviceCharge", 6);
        param.put("telephone", "18284533630");
        param.put("terminalUserId",82);
        param.put("userNmae","bobo");
        param.put("totalPrice","100");

        HttpClient httpclient =  HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(TestConst.DEV_CLOUDPOS_HTTP_V2 + "expresspayCard/record");
        FileBody userAvatar = new FileBody(new File(new String("E:\\1.jpg".getBytes())), "image/*");
        FileBody userAvatar1 = new FileBody(new File(new String("E:\\1.jpg".getBytes())), "image/*");
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + "da24aed9bc8e4744";
        System.out.println(newSignString);
        reqEntity.addPart("sign", new StringBody(MD5Util.encrypt(newSignString)));
        for (Map.Entry entry : param.entrySet()) {
            reqEntity.addPart(entry.getKey().toString(), new StringBody(entry.getValue().toString(), Charset.forName("utf-8")));
        }
      //  reqEntity.addPart("legalIdcardUrlFile", userAvatar);
      //  reqEntity.addPart("legalIdcardUrlBackFile", userAvatar1);
        httppost.setEntity(reqEntity);
        httppost.setHeader("token","5dd5e574ac4048a2a81208516726caa6");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(EntityUtils.toString(resEntity));
    }
    @Test
    public void testAdd1() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://localhost:8888/pemass-cloudpos-api/v1/expresspay/card/record/add");
        FileBody legalIdcardUrlFile = new FileBody(new File("E:\\1.jpg"),"image/*");
        FileBody legalIdcardUrlBackFile = new FileBody(new File("E:\\2.jpg"),"image/*");
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        reqEntity.addPart("terminalUserId", new StringBody("2"));
        reqEntity.addPart("cardNumber", new StringBody("5"));
        reqEntity.addPart("userNmae", new StringBody("石博", Charset.forName("utf-8")));
        reqEntity.addPart("telephone", new StringBody("18284533630"));
        reqEntity.addPart("totalPrice", new StringBody("200"));
        reqEntity.addPart("serviceCharge", new StringBody("6"));
        reqEntity.addPart("legalIdcard", new StringBody("123456789123456789"));
        reqEntity.addPart("legalIdcardUrlFile", legalIdcardUrlFile);
        reqEntity.addPart("legalIdcardUrlBackFile", legalIdcardUrlBackFile);
        reqEntity.addPart("invoiceTitle", new StringBody("单位1",Charset.forName("UTF-8")));
        reqEntity.addPart("receiveName", new StringBody("李政1",Charset.forName("UTF-8")));
        reqEntity.addPart("receiveAddress", new StringBody("1成都市高新区复城国际T4",Charset.forName("UTF-8")));
        reqEntity.addPart("receivePhone", new StringBody("11111"));
        httppost.setEntity(reqEntity);
        httppost.setHeader("token", "999999");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(EntityUtils.toString(resEntity));
    }
}