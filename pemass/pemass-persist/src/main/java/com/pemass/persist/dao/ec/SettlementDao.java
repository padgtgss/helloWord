/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.ec;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.ec.Settlement;

import java.util.List;

/**
 * @Description: SettlementDao
 * @Author: oliver.he
 * @CreateTime: 2015-08-21 15:01
 */
public interface SettlementDao extends BaseDao {
    /**
     * 根据出账入账统计出结算表数据
     * @return
     */
    List<Object[]> getGroupSettlementToTransaction();

    /**
     * 根据统计出来的数据条件查询结算表
     * @return
     */
    List<Settlement> getSettlementListByCount(Long targetBankCardId);
}