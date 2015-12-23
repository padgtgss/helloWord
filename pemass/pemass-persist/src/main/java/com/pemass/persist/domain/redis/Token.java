/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.redis;

import java.io.Serializable;

/**
 * @Description: Token
 * @Author: estn.zuo
 * @CreateTime: 2014-10-30 14:59
 */
public class Token implements Serializable {

    /*-- 本对象在redis数据库中的key前缀 --*/
    public static String REDIS_KEY = "token";

    private String targetUUID;  //目标对象UUID

    private String authority;   //权限(targetUUID对应用户的权限)

    private Long deviceId;  //设备

    private String token;   //token值

    private String refreshToken;    //刷新token值

    private Long targetId;//用户ID


    //===========================================getter and setter===========================================================


    public String getTargetUUID() {
        return targetUUID;
    }

    public void setTargetUUID(String targetUUID) {
        this.targetUUID = targetUUID;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
