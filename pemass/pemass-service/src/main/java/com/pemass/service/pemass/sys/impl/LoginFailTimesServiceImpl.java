/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.sys.impl;

import com.pemass.persist.dao.sys.LoginFailTimesDao;
import com.pemass.persist.domain.redis.LoginFailTimes;
import com.pemass.service.pemass.sys.LoginFailTimesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: LoginFailTimesServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-06-24 09:32
 */
@Service
public class LoginFailTimesServiceImpl implements LoginFailTimesService {

    @Resource
    private LoginFailTimesDao loginFailTimesDao;

    @Override
    public Boolean insert(LoginFailTimes loginFailTimes) {
        return loginFailTimesDao.insert(loginFailTimes);
    }

    @Override
    public Boolean clear(String username) {
        return loginFailTimesDao.clear(username);
    }

    @Override
    public Boolean increase(String username) {
        return loginFailTimesDao.increase(username);
    }

    @Override
    public Integer getLoginFailTimesByUsername(String username) {
        return loginFailTimesDao.getLoginFailTimesByUsername(username);
    }
}
