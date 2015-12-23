/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.redis;

import java.io.Serializable;

/**
 * @Description: 外部应用
 * @Author: pokl.huang
 * @CreateTime: 2015-04-09 11:23
 */
public class App implements Serializable  {

    /*-- 本对象在redis数据库中的key前缀 --*/
    public static String REDIS_KEY = "app";

    private String appid;//ID

    private String appname;//app名称

    private String key;//密钥

    private String createtime;//创建时间


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "App{" +
                "appid='" + appid + '\'' +
                ", appname='" + appname + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}

