/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.redis.App;

/**
 * @Description: UserService
 * @Author: estn.zuo
 * @CreateTime: 2014-10-30 15:52
 */
public interface AppService {

    /**
     * 根据appid获取app信息
     *
     * @param appid
     * @return
     */
    App getAppById(String appid);

    /**
     * 根据appName获取App对象
     *
     * @param appName 名字
     * @return 返回值
     */
    App getAppByName(String appName);

    /**
     * 保存一个APP信息<br/>
     * appid 和  secret 会自动生成
     *
     * @param app
     * @return
     */
    boolean saveApp(App app);


    /**
     * 更新一个APP信息<br/>
     * appid 和  secret 会自动生成
     *
     * @param app
     * @return
     */
    boolean updateApp(App app);

    /**
     * 删除一个APP信息<br/>
     * appid 和  secret 会自动生成
     *
     * @param appid
     * @return
     */
    boolean delApp(String appid);


    /**
     * 根据条件
     *
     * @param appid
     * @return
     */
    DomainPage getApps(String appid, Long pageIndex, Long pageSize);

}
