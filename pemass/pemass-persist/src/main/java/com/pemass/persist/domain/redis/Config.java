/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.redis;

import java.io.Serializable;

/**
 * @Description: Config
 * @Author: zhou hang
 * @CreateTime: 2015-05-26 10:34
 */
public class Config  implements Serializable  {

    /*-- 本对象在redis数据库中的key前缀 --*/
    public static String REDIS_KEY = "config";

    private String key; //key

    private String value;   //值

    private String title;   //标题

    private String createTime;//创建时间

    //====================================getter and  setter==============

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}