/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.sys;

import com.pemass.persist.domain.redis.LoginFailTimes;

/**
 * @Description: LoginFailTimesDao
 * @Author: estn.zuo
 * @CreateTime: 2015-06-23 17:41
 */
public interface LoginFailTimesDao {

    Boolean insert(LoginFailTimes loginFailTimes);

    Boolean clear(String username);

    Boolean increase(String username);

    Integer getLoginFailTimesByUsername(String username);
}
