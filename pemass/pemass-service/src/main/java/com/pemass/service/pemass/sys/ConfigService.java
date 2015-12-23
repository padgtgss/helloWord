/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;

/**
 * @Description: ConfigService
 * @Author: zhou hang
 * @CreateTime: 2015-05-26 10:38
 */
public interface ConfigService {
    /**
     * 查询所有基础配置
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<Config> search(long pageIndex, long pageSize);

    /**
     * 修改信息
     *
     * @param config
     */
    Config update(Config config);

    /**
     * 根据key 查询
     *
     * @param configEnum
     * @return
     */
    Config getConfigByKey(ConfigEnum configEnum);

    /**
     * 插入一条Config信息
     *
     * @param config
     * @return
     */
    boolean insert(Config config);
}
