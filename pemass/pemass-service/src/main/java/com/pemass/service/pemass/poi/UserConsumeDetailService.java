/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.persist.domain.jpa.poi.UserConsumeDetail;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

import java.util.List;

/**
 * @Description: UserConsumeDetailService
 * @Author: zhou hang
 * @CreateTime: 2015-06-26 09:47
 */
public interface UserConsumeDetailService {

    /**
     * 更新实际消耗积分数
     *
     * @param consumeType     消费类型
     * @param consumeTargetId 消费目标ID
     * @return
     */
    boolean updateAmount(ConsumeTypeEnum consumeType, Long consumeTargetId);

    /**
     * 新增积分消耗明细
     *
     * @param pointDetail 用户积分明细
     */
    void insertPointConsumeDetail(UserPointDetail pointDetail, Long poolId);

    /**
     * 根据消费类型和目标ID查询消费记录
     *
     * @param consumeType
     * @param consumeTargetId
     * @return
     */
    List<UserConsumeDetail> selectByConsumeTypeAndTargetId(ConsumeTypeEnum consumeType, Long consumeTargetId);

    /**
     * 根据积分池id获取该批的积分的核销总量
     *
     * @param pointPoolId 积分池id
     * @return
     */
    Integer getPointConsumeAmountByPointPoolId(Long pointPoolId);


    /**
     * 获取冻结的积分
     *
     * @param uid       用户
     * @param pointType 积分类型
     * @param isGeneral 是否是通用区域
     * @return
     */
    Integer getCongelationAmount(Long uid, PointTypeEnum pointType, Boolean isGeneral);

    /**
     * 根据订单id查询用户积分消耗明细
     * @param orderId
     * @return
     */
    List<UserConsumeDetail> getUserConsumeDetailListByOrderId(Long orderId,ConsumeTypeEnum consumeType,PointTypeEnum pointType);
}
