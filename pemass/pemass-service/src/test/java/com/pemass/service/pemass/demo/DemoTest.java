/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.demo;

import com.pemass.common.core.util.MD5Util;

/**
 * @Description: DemoTest
 * @Author: estn.zuo
 * @CreateTime: 2015-08-03 14:17
 */
public class DemoTest {

    public static void main(String[] args) {
//        int i = Seconds.secondsBetween(new DateTime(2015,8,7,12,12,12), new DateTime(2015,8,7,13,12,12)).getSeconds() ;
//        System.out.println(i);
//        String content = "我在云钱包（下载：http://t.cn/RwhACWb）获得了“韩国进口 KJ蜂蜜柚子茶1000g”，商品码：【电子票码】，分享给您。";
//        content = content.substring(0, content.indexOf("【")+1) + "123123123" + content.substring(content.indexOf("】"));
//        System.out.println(content);

        System.out.println(MD5Util.encrypt("123456","varzubei"));
    }
}
