/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: CollectMoneyStrategyDao
 * @Author: zhou hang
 * @CreateTime: 2014-12-26 21:46
 */
public interface CollectMoneyStrategyDao extends BaseDao {
    /**
     * 根据商家，查询出所有 策略
     *
     * @param terminalUserId
     * @return
     */
    List<Object[]> selectByTerminalUserId(Long terminalUserId);

    /**
     * 获取满足条件的策略
     *
     * @param conditions 查询条件
     * @return 返回结果
     */
    DomainPage getCollectMoneyStrategyByCondition(Map<String, Object> conditions, long pageIndex, long pageSize);

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
