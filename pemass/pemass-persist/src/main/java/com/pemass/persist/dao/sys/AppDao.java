/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.redis.App;

/**
 * @Description: AppDao
 * @Author: pokl.huang
 * @CreateTime: 2015-04-09 11:36
 */
public interface AppDao {

    App getAppById(String appid);

    boolean saveApp(App app);

    /**
     * 根据appName获取App对象
     *
     * @param appName 名字
     * @return 返回值
     */
    App getAppByName(String appName);

    boolean delApp(String appid);

    DomainPage getApps(String appid, Long pageIndex, Long pageSize);
}
