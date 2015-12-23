package com.pemass.service.pemass.poi;

import com.pemass.persist.domain.jpa.poi.PresentItem;
import com.pemass.persist.enumeration.PresentItemTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: PresentPackServiceImpl
 * @Author: he jun cheng
 * @CreateTime: 2014-10-13 10:35
 */
public interface PresentItemService {

    // ============================

    /**
     * 保存红包项
     *
     * @param presentItem
     */
    void savePresentItem(PresentItem presentItem);

    /**
     * 根据ID获取红包项信息
     *
     * @param presentItemId
     * @return
     */
    PresentItem getPresentItemById(Long presentItemId);

    /**
     * 更新红包项信息
     *
     * @param source
     * @return
     */
    PresentItem updatePresentItem(PresentItem source);

    /**
     * 根据ID逻辑删除红包项
     *
     * @param presentItemId
     * @return
     */
    PresentItem deletePresentItemById(Long presentItemId);

    // ============================


    /**
     * 根据红包id查询红包项列表
     *
     * @param map
     * @return
     */
    List<PresentItem> selectPresentItemList(Map<String, Object> map);

    /**
     * 根据认购批次id查询红包项
     *
     * @param pointPurchaseId
     * @return
     */
    List<PresentItem> selectPresentItemByPointPurchaseId(Long pointPurchaseId);

    /**
     * 根据红包批次id获取红包项的数量
     *
     * @param presntPackId 红包批次
     * @return
     */
    Integer getPresentItemAmountByPresentPackId(Long presntPackId);

    /**
     * 根据红包批次和积分类型获取该批红包的红包项中的总积分数量
     *
     * @param prsentPackId    红包批次
     * @param presentItemType 积分类型
     * @return
     */
    Integer getTotalPointByPresentPackId(Long prsentPackId, PresentItemTypeEnum presentItemType);
}
