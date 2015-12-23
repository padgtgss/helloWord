package com.pemass.persist.dao.bas;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.enumeration.DeviceTypeEnum;

/**
 * @Description: DeviceDao
 * @Author: cassiel.liu
 * @CreateTime: 2015-09-29 11:33
 */
public interface DeviceDao extends BaseDao {

    Long getDownloadAmountByType(DeviceTypeEnum deviceType);
}
