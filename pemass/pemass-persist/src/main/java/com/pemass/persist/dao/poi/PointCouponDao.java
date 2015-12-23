/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.PointCoupon;

import java.util.Map;

/**
 * @Description: PointCouponDao
 * @Author: cassiel.liu
 * @CreateTime: 2015-05-05 11:23
 */
public interface PointCouponDao {


    /**
     * 获取当天最大的积分券批次编号
     *
     * @param dataStr
     * @return
     */
    String getMaxIdentifier(String dataStr);

    /**
     * 分组查询积分券信息
     *
     * @param preciseMap 精确查询条件
     * @param fuzzyMap   模糊查询条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getCouponByGroup(Map<String, Object> preciseMap, Map<String, Object> fuzzyMap, long pageIndex, long pageSize);

    /**
     * 根据积分券的编号分页查询积分券信息
     *
     * @param preciseMap 精确查询条件
     * @param fuzzyMap   模糊查询条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getCouponByIdentifier(Map<String, Object> preciseMap, Map<String, Object> fuzzyMap, long pageIndex, long pageSize);

    /**
     * 根据批次号获取总记录数
     * @param identifier  批次号
     * @return
     */
    Long getCountCouponByIdentifier(String identifier);

    /**
     * 根据批次号，卡密获取积分券信息
     * @param packIdentifier
     * @param cardSecret
     * @return
     */
    PointCoupon selectCouponByFileds(String packIdentifier, String cardSecret);
}
