/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.ec;

import com.pemass.common.core.util.HttpUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import common.TestConst;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: OrderItemControllerTest
 * @Author: estn.zuo
 * @CreateTime: 2014-12-22 15:38
 */
public class OrderItemControllerTest {
    @Test
    public void testDetail() throws Exception {
        Map param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("orderItemId", "41");
        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "order_item/detail", param);
        System.out.println(result);
    }

    @Test
    public void overdue() {
        Map param = new HashMap();
        param.put("token", TestConst.TOKEN);
        param.put("uid", "1");
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order_item/overdue", param);
        System.out.println(result);
    }

    @Test
    public void list() {
        Map param = new HashMap();
        param.put("token", "9999");
        param.put("uid", 1);
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order_item/list", param);
        System.out.println(result);
    }


    @Test
    public void share() {
        Map param = new HashMap();
        param.put("token", "9999");
        param.put("orderItemId", 1540);
        String result = HttpUtil.post(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order_item/share", param);
        System.out.println(result);
    }


    @Test
    public void listV2() {
        Map param = new HashMap();
        //fc632a45cdb543d185178cbca4ad420c

        param.put("token", "fc632a45cdb543d185178cbca4ad420c");

        param.put("uid", 72L);
        param.put("appid", TestConst.APP_ID);
        param.put("stamp", UUID.randomUUID().toString());
        param.put("pageIndex", 1);
        param.put("pageSize", 100);
        param.put("useStatus", "UNUSED");
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param)) + "&" + TestConst.KEY;
        param.put("sign", MD5Util.encrypt(newSignString));
//        String result = HttpUtil.get(TestConst.TEST_CLOUDMONEY_HTTP_V2 + "order/item/search", param);
        String result = HttpUtil.get(TestConst.DEV_CLOUDMONEY_HTTP_V2 + "order/item/search", param);
        System.out.println(result);
    }

}
