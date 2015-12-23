package com.pemass.cloudpos.api.controller.sys;

import com.pemass.cloudpos.api.controller.TestConst;
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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by estn on 15/7/24.
 */
public class TerminalUserControllerTest {

    @Test
    public void testUpdatePassword() throws Exception {

    }

    @Test
    public void testUpdateEntiry() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("nickname", "malimali哄");
        param.put("gender", "FEMALE");
        param.put("email", "5696641@qq.com");
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("linkPhone", "18010550000");

        HttpClient httpclient =  HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(TestConst.DEV_CLOUDPOS_HTTP_V2 + "terminalUser/5");
        FileBody userAvatar = new FileBody(new File(new String("/Users/estn/Desktop/1.jpg".getBytes())), "image/*");
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        System.out.println(newSignString);
        reqEntity.addPart("sign", new StringBody(MD5Util.encrypt(newSignString)));
        for (Map.Entry entry : param.entrySet()) {
            reqEntity.addPart(entry.getKey().toString(), new StringBody(entry.getValue().toString(), Charset.forName("utf-8")));
        }
        reqEntity.addPart("avatarFile", userAvatar);
        httppost.setEntity(reqEntity);
        httppost.setHeader("token",TestConst.TOKEN_ROLE_CASHIER);

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(EntityUtils.toString(resEntity));
    }
}