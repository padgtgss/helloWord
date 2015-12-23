/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.dao.poi.PresentRecordDao;
import com.pemass.service.pemass.poi.PresentRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: PresentRecordServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2015-01-07 14:52
 */
@Service
public class PresentRecordServiceImpl implements PresentRecordService {

    @Resource
    private PresentRecordDao presentRecordDao;

    @Override
    public DomainPage selectByFromUserId(Long fromUserId, long pageIndex, long pageSize) {
        return presentRecordDao.selectByFromUserId(fromUserId, pageIndex, pageSize);
    }
}