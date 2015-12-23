/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.persist.dao.sys.AppDao;
import com.pemass.persist.domain.redis.App;
import com.pemass.service.pemass.sys.AppService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: AppServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-04-09 11:34
 */

@Service
public class AppServiceImpl implements AppService {

    @Resource
    private AppDao appDao;

    @Override
    public App getAppById(String appid) {
        return appDao.getAppById(appid);
    }

    @Override
    public App getAppByName(String appName) {
        return appDao.getAppByName(appName);
    }

    @Override
    public boolean saveApp(App app) {
        try {
            String appid = UUIDUtil.randomWithoutBar();
            app.setAppid(appid);
            String key = AESUtil.generateKey();
            app.setKey(key);

            appDao.saveApp(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateApp(App app) {
        try {
            appDao.saveApp(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delApp(String appid) {
        return appDao.delApp(appid);
    }



    @Override
    public DomainPage getApps(String appid,  Long pageIndex, Long pageSize) {
        return appDao.getApps(appid,pageIndex,pageSize);
    }


}

