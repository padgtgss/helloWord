/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: Token
 * @Author: estn.zuo
 * @CreateTime: 2014-11-10 10:12
 */
public class TokenPojo implements Serializable {

    private String token;

    private String refreshToken;

    private Date expiryTime;

    private TerminalUserPojo terminalUser;

    private UserPojo user;

    public UserPojo getUser() {
        return user;
    }

    public void setUser(UserPojo user) {
        this.user = user;
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

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public TerminalUserPojo getTerminalUser() {
        return terminalUser;
    }

    public void setTerminalUser(TerminalUserPojo terminalUser) {
        this.terminalUser = terminalUser;
    }
}
