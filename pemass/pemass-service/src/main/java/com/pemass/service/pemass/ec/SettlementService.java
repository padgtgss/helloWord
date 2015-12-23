/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.ec.Settlement;
import com.pemass.persist.domain.jpa.ec.Transaction;
import com.pemass.persist.domain.vo.SettlementStatisticsVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: SettlementService
 * @Author: oliver.he
 * @CreateTime: 2015-08-21 15:06
 */
public interface SettlementService {

    void insert(Settlement settlement);

    Settlement update(Settlement source);

    Settlement getById(Long settlementID);

    Settlement delete(Long settlementID);

    /**
     * 分页获取满足条件的纪录
     *
     * @param conditions 查询条件
     * @param domainPage 分页信息
     * @return 返回结果
     */
    DomainPage<Map<String, Object>> search(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取满足条件的全部结果
     *
     * @param conditions 查询条件
     * @return 返回结果
     */
    List<Map<String, Object>> selectAllSettlement(Map<String, Object> conditions);

    /**
     * 根据Settlement集合分页查询
     *
     * @param ids       id集合
     * @param pageIndex 页码
     * @param pageSize  每页显示纪录
     * @return 返回结果
     */
    DomainPage selectSettlementInfoByIds(List<Long> ids, long pageIndex, long pageSize);


    /**
     * 按商户统计出账总额和入帐总额
     *
     * @return 返回结果
     */
    DomainPage<SettlementStatisticsVO> statistics(Map<String, Object> conditions);

    /**
     * 根据结算表数据分组到交易表
     *
     * @return
     */
    List<Transaction> getGroupSettlementToTransaction();

    /**
     * 根据条件查询结算表
     *
     * @param fieldNameValueMap
     * @return
     */
    List<Settlement> getSettlementListByMap(Map<String, Object> fieldNameValueMap);

    /**
     * 根据统计出来的数据条件查询结算表
     * @return
     */
    List<Settlement> getSettlementListByCount(Long targetBankCardId);
}