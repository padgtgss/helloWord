/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.dao.sys.ConfigDao;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.sys.ConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: ConfigServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2015-05-26 10:39
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigDao configDao;

    @Override
    public DomainPage<Config> search(long pageIndex, long pageSize) {
        return configDao.search(pageIndex, pageSize);
    }

    @Override
    public Config update(Config config) {
        Preconditions.checkNotNull(config);
        configDao.update(config);
        return config;
    }

    @Override
    public Config getConfigByKey(ConfigEnum configEnum) {
        Preconditions.checkNotNull(configEnum);
        Config config = configDao.getByKey(configEnum);
        if (config == null) {
            throw new BaseBusinessException(SysError.CONFIG_NOT_EXIST);
        }
        return config;
    }

    @Override
    public boolean insert(Config config) {
        Preconditions.checkNotNull(config);
        return configDao.insert(config);
    }
}