/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

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

/**
 * @Description: UserControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2014-12-08 18:27
 */
public class UserControllerTest {

    @Test
    public void testDetail() throws Exception {
        Map<String, Object> map = new HashMap();
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "presentSquare/show", map);
        System.out.println(result);
    }

    @Test
    public void testEmailValidate() throws Exception {

        Map<String, Object> map = new HashMap();
        map.put("appid", TestConst.APP_ID);
        map.put("stamp", UUID.randomUUID().toString());
        map.put("email", "estn.zuo@pemass.com");
        map.put("uid", "6");
        map.put("token", TestConst.TOKEN);
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "user/email/validate", map);
        System.out.println(result);
    }

    @Test
    public void testUpdateEmail() throws Exception {
        Map<String, Object> map = new HashMap();
        map.put("appid", TestConst.APP_ID);
        map.put("stamp", UUID.randomUUID().toString());
        map.put("email", "estn.zuo@pemass.com");
        map.put("token", TestConst.TOKEN);
        map.put("uid", "6");
        map.put("validateCode", "818121");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(map)) + "&" + TestConst.KEY;
        map.put("sign", MD5Util.encrypt(newSignString));
        String result = HttpUtil.post(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "user/email", map);
        System.out.println(result);
    }

    @Test
    public void testUpdate() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("nickname", "malimali哄");
        param.put("gender", "FEMALE");
        param.put("email", "");
        param.put("birthday", "2014-10-10 00:00:00");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", "8a5eb7a-50f1-4c09-8c59-cad95a5933d8");

        HttpClient httpclient =  HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "user/32");
        FileBody userAvatar = new FileBody(new File(new String("/Users/estn/Desktop/3.jpg".getBytes())), "image/*");
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        System.out.println(newSignString);
        reqEntity.addPart("sign", new StringBody(MD5Util.encrypt(newSignString)));
        for (Map.Entry entry : param.entrySet()) {
            reqEntity.addPart(entry.getKey().toString(), new StringBody(entry.getValue().toString(), Charset.forName("utf-8")));
        }
        reqEntity.addPart("avatarFile", userAvatar);
        httppost.setEntity(reqEntity);
        httppost.setHeader("token","bf83ebbded5b491cb62d750d89956eb2");

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(EntityUtils.toString(resEntity));
    }

    @Test
    public void test2() {

    }
}
