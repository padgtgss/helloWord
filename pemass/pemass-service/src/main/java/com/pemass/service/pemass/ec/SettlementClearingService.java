/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.ec;

import com.pemass.persist.domain.jpa.ec.SettlementClearing;

import java.util.List;

/**
 * @Description: SettlementClearingService
 * @Author: luoc
 * @CreateTime: 2015-08-27 09:40
 */
public interface SettlementClearingService {
    /**
     * 保存清分结算中间表
     *
     * @param settlementClearing
     */
    void insert(SettlementClearing settlementClearing);

    /**
     * 根据“结算”id查询和器相关的“清算”id
     * @param settlementId
     * @return
     */
    List<Long> selectClearingIdsBySettlementId(Long settlementId);


}