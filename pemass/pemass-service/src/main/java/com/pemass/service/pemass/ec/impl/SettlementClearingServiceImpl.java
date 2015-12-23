package com.pemass.service.pemass.ec.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.ec.SettlementClearing;
import com.pemass.service.pemass.ec.SettlementClearingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: SettlementClearingServiceImpl
 * @Author: luoc
 * @CreateTime: 2015-08-27 09:48
 */
@Service
public class SettlementClearingServiceImpl implements SettlementClearingService {

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public void insert(SettlementClearing settlementClearing) {
        checkNotNull(settlementClearing);
        jpaBaseDao.persist(settlementClearing);
    }

    @Override
    public List<Long> selectClearingIdsBySettlementId(Long settlementId) {
        Preconditions.checkNotNull(settlementId);
        List<Long> ids = Lists.newArrayList();
        List<SettlementClearing> settlements = jpaBaseDao.getEntitiesByField(SettlementClearing.class, "settlementId", settlementId);
        if (settlements.size() < 1) {
            ids.add(0L);
        } else {
            for (SettlementClearing settlementClearing : settlements) {
                ids.add(settlementClearing.getClearingId());
            }
        }
        return ids;
    }
}
