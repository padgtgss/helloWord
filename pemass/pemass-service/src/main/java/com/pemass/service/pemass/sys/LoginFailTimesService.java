/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.redis.LoginFailTimes;

/**
 * @Description: LoginFailTimesService
 * @Author: estn.zuo
 * @CreateTime: 2015-06-24 09:23
 */
public interface LoginFailTimesService {

    Boolean insert(LoginFailTimes loginFailTimes);

    Boolean clear(String username);

    Boolean increase(String username);

    Integer getLoginFailTimesByUsername(String username);
}
