/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.redis;

import java.io.Serializable;

/**
 * @Description: 用户登录失败次数
 * @Author: estn.zuo
 * @CreateTime: 2015-06-23 17:38
 */
public class LoginFailTimes implements Serializable {

    /*-- 本对象在redis数据库中的key前缀 --*/
    public static String REDIS_KEY = "login_fail_times";

    private String username;//用户名

    private Integer times;//失败次数

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
