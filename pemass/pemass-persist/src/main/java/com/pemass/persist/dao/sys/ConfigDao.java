/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;

/**
 * @Description: TokenDao
 * @Author: estn.zuo
 * @CreateTime: 2015-04-09 18:59
 */
public interface ConfigDao {

    /**
     * 插入一条Config信息
     *
     * @param config
     * @return
     */
    boolean insert(Config config);

    /**
     * 更新一个Config信息
     *
     * @param config
     * @return
     */
    Config update(Config config);

    /**
     * 根据config的key获取config
     *
     * @param keyEnum
     * @return
     */
    Config getByKey(ConfigEnum keyEnum);


    /**
     * 分页获取Config信息
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<Config> search(Long pageIndex, Long pageSize);

}
