package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategy;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 收款策略
 * Created by hejch on 2014/12/25.
 */
public interface CollectMoneyStrategyService {

    /**
     * 查询商家的有效策略
     *
     * @param organizationId 商家ID
     * @return
     */
    List<CollectMoneyStrategy> selectValidStrategyByOrganizationId(Long organizationId);


    /**
     * 查询商家当前的有效策略
     *
     * @param organizationId
     * @return
     */
    CollectMoneyStrategy getCurrentStrategyByOrganizationId(Long organizationId);

    /**
     * 获取收款策略记录
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage getStrategyByConditions(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取商户段的收款策略
     *
     * @param organizationId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectByOrganizationId(Long organizationId, long pageIndex, long pageSize);

    /**
     * 新增收款策略
     *
     * @param collectMoneyStrategy
     * @param schemes
     */
    void insertStrategy(CollectMoneyStrategy collectMoneyStrategy, Map<String, Object> schemes);

    /**
     * 更新收款策略
     *
     * @param collectMoneyStrategy
     * @return
     */
    CollectMoneyStrategy updateStrategy(CollectMoneyStrategy collectMoneyStrategy, Map<String, Object> schemes);

    /**
     * 设置自定义收款策略无效
     *
     * @param collectMoneyStrategy
     * @return
     */
    CollectMoneyStrategy updateStrategyInvalid(CollectMoneyStrategy collectMoneyStrategy);

    /**
     * 获取收款策略详情
     *
     * @param collectMoneyStrategyId
     * @return
     */
    CollectMoneyStrategy getById(Long collectMoneyStrategyId);

    /**
     * 根据商户id,查询出商户的所有“策略”
     *
     * @param terminalUserId 商家id
     * @return
     */
    CollectMoneyStrategySnapshot selectByTerminalUserId(Long terminalUserId);

    /**
     * 查询策略明细
     *
     * @param collectMoneyStrategyId 策略id
     * @return
     */
    CollectMoneyStrategy getByDetail(Long collectMoneyStrategyId);

    /**
     * 根据策略和金额，计算出自定义相关明细
     *
     * @param terminalUserId         收银员id
     * @param collectMoneyStrategyId 策略id
     * @param username               用户(手机号)
     * @param consumptionAmount      消费金额
     * @return
     */
    Long insertCompute(Long terminalUserId, Long collectMoneyStrategyId, String username, Float consumptionAmount);

    /**
     * 查询所有收款策略
     *
     * @param map
     * @return
     */
    List<CollectMoneyStrategy> selectCollectMoneyStrategyList(Map<String, Object> map);

    /**
     * 删除当前收款策略
     *
     * @param collectMoneyStrategyId 策略id
     */
    void deleteCollectMoneyStrategy(Long collectMoneyStrategyId);

    void updateCollectMoneyStrategy();

    /**
     * 校验商户的自定义收款策略
     *
     * @param organizationId   商户ID
     * @param startTime        开始时间
     * @param endTime          结束时间
     * @param executeStartTime 开始时间点
     * @param executeEndTime   结束时间点
     * @return 校验结果
     */
    boolean checkStrategy(Long organizationId, Date startTime, Date endTime, String executeStartTime, String executeEndTime);

}
