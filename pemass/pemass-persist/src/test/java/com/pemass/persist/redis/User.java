/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.redis;

import java.io.Serializable;

/**
 * @Description: User
 * @Author: pokl.huang
 * @CreateTime: 2015-04-08 14:48
 */
public class User implements Serializable {
    String userid;
    String username;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

