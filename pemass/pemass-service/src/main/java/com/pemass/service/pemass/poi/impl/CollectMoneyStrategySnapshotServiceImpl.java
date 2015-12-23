/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.poi.CollectMoneyScheme;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategy;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.service.pemass.poi.CollectMoneySchemeService;
import com.pemass.service.pemass.poi.CollectMoneyStrategySnapshotService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: CollectMoneyStrategySnapshotServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-08-03 16:11
 */
@Service
public class CollectMoneyStrategySnapshotServiceImpl implements CollectMoneyStrategySnapshotService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private CollectMoneySchemeService collectMoneySchemeService;

    @Resource
    private OrganizationService organizationService;

    @Override
    public CollectMoneyStrategySnapshot insertFromStrategy(CollectMoneyStrategy strategy) {
        Preconditions.checkNotNull(strategy);
        Preconditions.checkNotNull(strategy.getOrganizationId());
        Organization organization = organizationService.getOrganizationById(strategy.getOrganizationId());

        CollectMoneyStrategySnapshot strategySnapshot = new CollectMoneyStrategySnapshot();
        MergeUtil.merge(strategy, strategySnapshot);
        strategySnapshot.setId(null);
        strategySnapshot.setOrganizationName(organization.getOrganizationName());
        strategySnapshot.setCollectMoneyStrategyId(strategy.getId());
        //设置E积分描述
        strategySnapshot.setGivingPonitPDesc("无");
        if (strategy.getIsGivingPointP() == 1) {
            CollectMoneyScheme scheme = collectMoneySchemeService.getById(strategy.getPointPSchemeId());

            switch (scheme.getSchemeType()) {
                case PERCENTAGE:
                    strategySnapshot.setGivingPonitPDesc("满" + scheme.getMiniAmount() + "送" + scheme.getMiniGiveAmount() + "积分");
                    break;
                case SHOPPING_AMOUNT:
                    strategySnapshot.setGivingPonitPDesc("赠送金额" + scheme.getConversionFactor() * 100 + "%的积分");
                    break;
                case IMMOBILIZATION:
                    strategySnapshot.setGivingPonitPDesc("固定赠送" + scheme.getImmobilizationPresented() + "积分");
                    break;
            }

        }
        //设置E通币描述
        strategySnapshot.setGivingPonitEDesc("无");
        if (strategy.getIsGivingPointE() == 1) {
            CollectMoneyScheme scheme = collectMoneySchemeService.getById(strategy.getPointESchemeId());
            switch (scheme.getSchemeType()) {
                case PERCENTAGE:
                    strategySnapshot.setGivingPonitEDesc("满" + scheme.getMiniAmount() + "送" + scheme.getMiniGiveAmount() + "通币");
                    break;
                case SHOPPING_AMOUNT:
                    strategySnapshot.setGivingPonitEDesc("赠送金额" + scheme.getConversionFactor() * 100 + "%的通币");
                    break;
                case IMMOBILIZATION:
                    strategySnapshot.setGivingPonitEDesc("固定赠送" + scheme.getImmobilizationPresented() + "通币");
                    break;
            }
        }
        jpaBaseDao.persist(strategySnapshot);
        return strategySnapshot;
    }

    @Override
    public CollectMoneyStrategySnapshot getById(Long collectMoneyStrategySnapshotId) {
        Preconditions.checkNotNull(collectMoneyStrategySnapshotId);
        return jpaBaseDao.getEntityById(CollectMoneyStrategySnapshot.class, collectMoneyStrategySnapshotId);
    }
}
