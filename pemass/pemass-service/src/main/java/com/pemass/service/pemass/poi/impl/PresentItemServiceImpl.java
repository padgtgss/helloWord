/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentItemDao;
import com.pemass.persist.domain.jpa.poi.PresentItem;
import com.pemass.persist.enumeration.PresentItemTypeEnum;
import com.pemass.service.pemass.poi.PresentItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: PresentItemServiceImpl
 * @Author: Administrator
 * @CreateTime: 2014-12-29 19:10
 */
@Service
public class PresentItemServiceImpl implements PresentItemService {

    @Resource
    private BaseDao jpaBaseDao;
    @Resource
    private PresentItemDao presentItemDao;

    // ============================
    @Override
    public void savePresentItem(PresentItem presentItem) {
        Preconditions.checkNotNull(presentItem);
        jpaBaseDao.persist(presentItem);
    }

    @Override
    public PresentItem getPresentItemById(Long presentItemId) {
        Preconditions.checkNotNull(presentItemId);
        return jpaBaseDao.getEntityById(PresentItem.class, presentItemId);
    }

    @Override
    public PresentItem updatePresentItem(PresentItem source) {
        Preconditions.checkNotNull(source);
        PresentItem targetPresentItem = getPresentItemById(source.getId());
        MergeUtil.merge(source, targetPresentItem);
        return jpaBaseDao.merge(targetPresentItem);
    }

    @Override
    public PresentItem deletePresentItemById(Long presentItemId) {
        Preconditions.checkNotNull(presentItemId);
        PresentItem targetPresentItem = getPresentItemById(presentItemId);
        targetPresentItem.setAvailable(AvailableEnum.UNAVAILABLE);
        targetPresentItem.setUpdateTime(new Date());
        return jpaBaseDao.merge(targetPresentItem);
    }
    // ============================

    @Override
    public List<PresentItem> selectPresentItemList(Map<String, Object> map) {
        return jpaBaseDao.getEntitiesByFieldList(PresentItem.class, map);
    }

    @Override
    public List<PresentItem> selectPresentItemByPointPurchaseId(Long pointPurchaseId) {
        return jpaBaseDao.getEntitiesByField(PresentItem.class, "pointPurchase.id", pointPurchaseId);
    }

    @Override
    public Integer getPresentItemAmountByPresentPackId(Long presntPackId) {
        Preconditions.checkNotNull(presntPackId);
        return presentItemDao.getPresentItemByPresentPackId(presntPackId);
    }

    @Override
    public Integer getTotalPointByPresentPackId(Long prsentPackId, PresentItemTypeEnum presentItemType) {
        Preconditions.checkNotNull(prsentPackId);
        Preconditions.checkNotNull(presentItemType);
        return presentItemDao.getTotalPointByPresentPackId(prsentPackId, presentItemType);
    }

}