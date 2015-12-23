/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.Version;
import com.pemass.persist.enumeration.AppTypeEnum;
import com.pemass.persist.enumeration.DeviceTypeEnum;

/**
 * @Description: VersionService
 * @Author: estn.zuo
 * @CreateTime: 2014-11-24 15:40
 */
public interface VersionService {

    /**
     * 当前版本和最新版本比较
     *
     * @param currentBuildNumber 当前构建版本号
     * @param appType            客户端类型B/C
     * @param deviceType         设备类型
     * @return
     */
    Version compareWithLatestVersion(Integer currentBuildNumber, AppTypeEnum appType, DeviceTypeEnum deviceType);

    /**
     * 新增一个版本
     *
     * @param version
     * @return
     */
    Version insert(Version version);

    /**
     * 分页查询版本
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<Version> select(long pageIndex, long pageSize);


    Version update(Version version);

}
