/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.poi.OnePointConsumeDetail;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;

import java.util.List;

/**
 * @Description: OnePointConsumeDetailService
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:48
 */
public interface OnePointConsumeDetailService {


    /**
     * 查询消费记录
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage consumeDetail(Long uid, Long pageIndex, Long pageSize);

    /**
     * 根据用户ID 统计当前用户的 已赚还款数
     * @param uid 用户Id
     * @return
     */
    Double  getPointCounsumeDetailCount(Long uid);

    /**
     * 分别获取自己赚的跟朋友赚的 还款数
     * @param uid
     * @return
     */
    Object[] getProfitCount(Long uid);

    /**
     * 朋友跟自己花费积分赚取还款额详情
     * @param uid 当前用户
     * @param isYouself 是否是当前用户自己赚取的详情（true:自己，false：朋友）
     * @param pageIndex
     * @param pageSizeL
     * @return
     */
    DomainPage selectProfitDetails(long uid,boolean isYouself,long pageIndex,long pageSize);

    /**
     * 查询当前用户为朋友花费的壹购积分记录
     * @param uid 当前用户
     * @param belongUserName  朋友手机号
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectForFriendDetail(Long uid,String belongUserName,long pageIndex,long pageSize);

    /**
     * 根据积分明细新增积分消耗记录
     * @param onePointDetail 积分明细记录
     * @param amount   消耗的积分数量
     */
    void insertDetail(OnePointDetail onePointDetail,Order order,Integer amount);

    /**
     * 根据订单编号获取消耗记录
     * @param orderId
     * @return
     */
    List<OnePointConsumeDetail> selectByOrderId(Long orderId);

    /**
     * 朋友赠送的壹购积分冻结数量
     * @param uid
     * @return
     */
    Long getFreeGivingPointAmount(Long uid);
}