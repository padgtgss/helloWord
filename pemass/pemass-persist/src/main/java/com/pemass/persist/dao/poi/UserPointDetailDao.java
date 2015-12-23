/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;


import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.enumeration.PointTypeEnum;

import java.util.List;

/**
 * @Description: UserPointDetailDao
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 14:38
 */
public interface UserPointDetailDao {


    List<UserPointDetail> selectPointCount(Long uid);

    /**
     * 获取可用积分中包含某个商户认购的某一批积分的用户信息
     *
     * @param pointPurchaseIds 商户认购的所有积分批次的id集合
     * @param pageIndex        当前页
     * @param pageSize         页大小
     * @return
     */
    DomainPage selectUsersByPointPurchaseId(List<Long> pointPurchaseIds, long pageIndex, long pageSize);

    /**
     * 查询用户的可用的积分
     *
     * @param uid
     * @param typeEnum
     * @return
     */
    List<UserPointDetail> selectByUserId(Long uid, PointTypeEnum typeEnum);

    /**
     * 查询用户所有的派
     *
     * @param uid
     * @return
     */
    Long getEntityById(Long uid);

    /**
     * 根据积分类型查询积分收支明细
     * <p/>
     * 1.获取E通币明细
     * 2.获取通用E积分明细
     *
     * @param uid
     * @param pointType
     * @return
     */
    DomainPage selectUserPointDetail(Long uid, PointTypeEnum pointType, long pageIndex, long pageSize);

    /**
     * 归还用户积分
     *
     * @param id
     * @param payableAmount
     */
    void usePointReturn(Long id, Integer payableAmount);

    /**
     * 查询用户过期的积分明细
     *
     * @return
     */
    List<UserPointDetail> selectExpireUserPointDetail();

    /**
     * 分别统计各个积分发行方  发行的定向积分
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectDirectionPointDetailByPool(Long uid, long pageIndex, long pageSize);


    /**
     * 获取当前用户某种定向积分的收支详情
     *
     * @param uid         用户
     * @param pointPoolId 定向积分发放方Id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectUserDirectionPointDetail(Long uid, Long pointPoolId, long pageIndex, long pageSize);

    /**
     * 分批次获取可用E积分明细
     *
     * @param uid
     * @param isDirection 是否是获取定向积分
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectUserPointDetailP(Long uid, boolean isDirection, long pageIndex, long pageSize);


    Integer getUserPointDetailGeneralPCount(Long uid);

    Integer getUserPointDetailECount(Long uid);

    Integer getPointAmountByPointPoolId(Long pointPoolId);

    Integer getExpiredAmountByPointPoolId(Long pointPoolId);


    void updateUseableAmountById(Long id, Integer useableAmount);
}
