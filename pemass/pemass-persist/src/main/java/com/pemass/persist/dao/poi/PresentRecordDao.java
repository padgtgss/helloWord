/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;

/**
 * @Description: PresentRecordDao
 * @Author: zhou hang
 * @CreateTime: 2015-07-01 10:32
 */
public interface PresentRecordDao extends BaseDao {
    /**
     * 查询赠送红包记录
     * @param fromUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectByFromUserId(Long fromUserId, long pageIndex, long pageSize);

}
