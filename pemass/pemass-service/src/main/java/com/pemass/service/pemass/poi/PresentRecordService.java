/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;

/**
 * @Description: PresentRecordService
 * @Author: zhou hang
 * @CreateTime: 2015-01-07 14:49
 */
public interface PresentRecordService {
    /**
     * 查询用户赠送记录
     *
     * @param fromUserId 用户
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectByFromUserId(Long fromUserId, long pageIndex, long pageSize);
}
