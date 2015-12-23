package com.pemass.service.pemass.poi;

import com.pemass.persist.domain.jpa.poi.OrganizationConsumeDetail;
import com.pemass.persist.enumeration.ConsumeTypeEnum;

import java.util.List;

/**
 * @Description: 商户积分消耗
 * @Author: cassiel.liu
 * @CreateTime: 2015-07-16 22:12
 */
public interface OrganizationConsumeDetailService {

    /**
     * 新增记录
     *
     * @param organizationConsumeDetail
     * @return
     */
    OrganizationConsumeDetail insert(OrganizationConsumeDetail organizationConsumeDetail);

    /**
     * 根据消费类型和目标ID查询消费记录
     *
     * @param consumeType
     * @param consumeTargetId
     * @return
     */
    List<OrganizationConsumeDetail> selectByConsumeTypeAndTargetId(ConsumeTypeEnum consumeType, Long consumeTargetId);


    /**
     * 更新实际消耗积分数
     *
     * @param consumeType     消费类型
     * @param consumeTargetId 消费目标ID
     * @return
     */
    boolean updateAmount(ConsumeTypeEnum consumeType, Long consumeTargetId);

    /**
     * 根据红包批次id，批量修改消耗明细
     *
     * @param auditStatus   红包的审核状态(0-失败，1-成功)
     * @param consumeType   消耗类型
     * @param presentPackId 消耗的目标id
     * @return
     */
    boolean updateByPresentPackId(Integer auditStatus, ConsumeTypeEnum consumeType, Long presentPackId);

    /**
     * 根据积分池的id查询出，该批次积分商户未成功消耗的数量
     *
     * @param pointPoolId 积分池id
     * @return
     */
    Integer getUnsuccessfulConsumeAmountByPointPoolId(Long pointPoolId);


    /**
     * 删除一条商户积分消耗明细记录
     *
     * @param consumeDetailId
     */
    void deleteConsumeDetail(Long consumeDetailId);

    /**
     * 根据红包批次的id统计该商户的积分消耗条数
     * @param presentPackId 红包批次id
     * @param consumeType  消耗类型
     * @return
     */
    Integer getCountConsumeAmountByPresentPackId( Long presentPackId, ConsumeTypeEnum consumeType);

}
