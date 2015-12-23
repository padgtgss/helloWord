/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.PointCoupon;

import java.util.Map;

/**
 * @Description: PointCouponService
 * @Author: cassiel.liu
 * @CreateTime: 2015-05-05 10:23
 */
public interface PointCouponService {

    /**
     * 分组分页查询积分券信息
     *
     * @param preciseMap 精确查询条件
     * @param fuzzyMap   模糊查询条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectCouponByGroup(Map<String, Object> preciseMap, Map<String, Object> fuzzyMap, long pageIndex, long pageSize);

    /**
     * 根据积分券编号分页查询积分券信息
     *
     * @param preciseMap 精确查询条件
     * @param fuzzyMap   模糊查询条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectCouponByIdentifier(Map<String, Object> preciseMap, Map<String, Object> fuzzyMap, long pageIndex, long pageSize);

    /**
     * 插入
     *
     * @param pointCoupon
     * @return
     */
    Boolean insert(PointCoupon pointCoupon, Integer amount);

    /**
     * 生成批号
     *
     * @return
     */
    String getPointCouponIdentifier();

    /**
     * 生成卡密
     *
     * @return
     */
    String getCardSecret();

    /**
     * 根据积分券批次，卡密充值
     * @param packIdentifier 积分券批次
     * @param cardSecret 积分券卡密
     * @return
     */
    Boolean couponRecharge(String packIdentifier,String cardSecret);
}
