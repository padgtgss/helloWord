/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.CollectMoneyStrategyDao;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.poi.CollectMoneyScheme;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategy;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.service.exception.PoiError;
import com.pemass.service.pemass.poi.CollectMoneySchemeService;
import com.pemass.service.pemass.poi.CollectMoneyStrategyService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: CollectMoneyStrategyServiceImpl
 * @Author: he jun cheng
 * @CreateTime: 2014-12-25 09:44
 */
@Service
public class CollectMoneyStrategyServiceImpl implements CollectMoneyStrategyService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private CollectMoneyStrategyDao collectMoneyStrategyDao;

    @Resource
    private CollectMoneySchemeService collectMoneySchemeService;

    @Resource
    private OrganizationService organizationService;


    @Override
    public List<CollectMoneyStrategy> selectValidStrategyByOrganizationId(Long organizationId) {
        Expression organizationExpression = new Expression("organizationId", Operation.Equal, organizationId);
        Expression isInvalidExpression = new Expression("isValid", Operation.Equal, 1);
        Expression startTimeExpression = new Expression("startTime", Operation.LessThanEqual, new Date());
        Expression endTimeExpression = new Expression("endTime", Operation.GreaterThanEqual, DateTime.now().millisOfDay().withMinimumValue().toDate());
        List<Expression> expressions = Lists.newArrayList(organizationExpression, isInvalidExpression, startTimeExpression, endTimeExpression);

        @SuppressWarnings("unchecked")
        List<CollectMoneyStrategy> strategies = jpaBaseDao.getEntitiesByExpressionList(CollectMoneyStrategy.class, expressions);
        return strategies;
    }

    @Override
    @SuppressWarnings("all")
    public CollectMoneyStrategy getCurrentStrategyByOrganizationId(Long organizationId) {
        Preconditions.checkNotNull(organizationId);
        List<CollectMoneyStrategy> strategies = this.selectValidStrategyByOrganizationId(organizationId);
        if (strategies == null || strategies.size() == 0) {
            return null;
        }

        for (CollectMoneyStrategy strategy : strategies) {

            //比较日期
            long now = System.currentTimeMillis();
            long startTime = strategy.getStartTime().getTime();
            long endTime = new DateTime(strategy.getEndTime()).millisOfDay().withMaximumValue().getMillis();
            if (now < startTime || now > endTime) {
                continue;
            }

            //比较时间
            int minuteOfDay = DateTime.now().getMinuteOfDay();
            String executeStartTime = strategy.getExecuteStartTime();
            String executeEndTime = strategy.getExecuteEndTime();
            if (executeStartTime == null || executeEndTime == null) {
                throw new BaseBusinessException(PoiError.STRATEGY_FORMAT_ERROR);
            }
            List<String> startTimeList = Splitter.on(":").omitEmptyStrings().trimResults().splitToList(executeStartTime);
            List<String> endTimeList = Splitter.on(":").omitEmptyStrings().trimResults().splitToList(executeEndTime);
            if (startTimeList.size() != 2 || endTimeList.size() != 2) {
                throw new BaseBusinessException(PoiError.STRATEGY_FORMAT_ERROR);
            }

            int startMinuteOfDay = Integer.parseInt(startTimeList.get(0)) * 60 + Integer.parseInt(startTimeList.get(1));
            int endMinuteOfDay = Integer.parseInt(endTimeList.get(0)) * 60 + Integer.parseInt(endTimeList.get(1));
            if (minuteOfDay > startMinuteOfDay && minuteOfDay < endMinuteOfDay) {
                return strategy;
            }
        }

        return null;
    }

    @Override
    public DomainPage getStrategyByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        /** 获取满足条件的结果 **/
        DomainPage resultDomainPage = collectMoneyStrategyDao.getCollectMoneyStrategyByCondition(conditions, domainPage.getPageIndex(), domainPage.getPageSize());

        /** 完善结果 **/
        @SuppressWarnings("unchecked") List<CollectMoneyStrategy> strategies = resultDomainPage.getDomains();
        List<Map<String, Object>> newStrategies = Lists.newArrayList();
        for (CollectMoneyStrategy strategy : strategies) {
            Map<String, Object> newStrategy = Maps.newHashMap();
            newStrategy.put("strategy", strategy);
            if (strategy.getPointPSchemeId() != null) {
                CollectMoneyScheme paiScheme = collectMoneySchemeService.getById(strategy.getPointPSchemeId());
                newStrategy.put("paiScheme", paiScheme);
            }
            if (strategy.getPointESchemeId() != null) {
                CollectMoneyScheme beiScheme = collectMoneySchemeService.getById(strategy.getPointESchemeId());
                newStrategy.put("beiScheme", beiScheme);
            }
            newStrategies.add(newStrategy);
        }

        /** 封装并返回结果 **/
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(resultDomainPage.getPageSize(), resultDomainPage.getPageIndex(), resultDomainPage.getTotalCount());
        returnDomainPage.setDomains(newStrategies);
        return returnDomainPage;
    }

    @Override
    @SuppressWarnings("all")
    public DomainPage selectByOrganizationId(Long organizationId, long pageIndex, long pageSize) {
        Preconditions.checkNotNull(organizationId);
        DomainPage domainPage = jpaBaseDao.getEntitiesPagesByExpression(CollectMoneyStrategy.class, new Expression("organizationId", Operation.Equal, organizationId), pageIndex, pageSize);
        return domainPage;
    }

    @Override
    public void insertStrategy(CollectMoneyStrategy collectMoneyStrategy, Map<String, Object> schemes) {
        Preconditions.checkNotNull(collectMoneyStrategy);
        //默认使用E通币抵扣
        collectMoneyStrategy.setIsDeductionPointE(1);
        //只有当进行折扣的时候才进行抵扣E积分
        collectMoneyStrategy.setIsDeductionPointP(0);

        if (collectMoneyStrategy.getIsDiscount() != null) {
            collectMoneyStrategy.setIsDeductionPointP(1);
        }
        CollectMoneyScheme paiCollectMoneyScheme = (CollectMoneyScheme) schemes.get("paiCollectMoneyScheme");
        CollectMoneyScheme beiCollectMoneyScheme = (CollectMoneyScheme) schemes.get("beiCollectMoneyScheme");
        if (collectMoneyStrategy.getIsGivingPointP() == 1 && paiCollectMoneyScheme != null) {
            jpaBaseDao.persist(paiCollectMoneyScheme);
            collectMoneyStrategy.setPointPSchemeId(paiCollectMoneyScheme.getId());
        }
        if (collectMoneyStrategy.getIsGivingPointE() == 1 && beiCollectMoneyScheme != null) {
            jpaBaseDao.persist(beiCollectMoneyScheme);
            collectMoneyStrategy.setPointESchemeId(beiCollectMoneyScheme.getId());
        }
        // 对开始时间和结束时间进行处理
        dateHandle(collectMoneyStrategy);

        // 处理折扣返回两位笑数
        int isDiscount = collectMoneyStrategy.getIsDiscount();
        Double discount = collectMoneyStrategy.getDiscount();
        if (isDiscount == 1 && discount != null) {
            collectMoneyStrategy.setDiscount(ArithmeticUtil.ceil(discount, 2));
        }

        jpaBaseDao.persist(collectMoneyStrategy);
    }

    /**
     * 对开始时间和结束时间进行处理
     */
    private void dateHandle(CollectMoneyStrategy collectMoneyStrategy) {
        Date oldStartDate = collectMoneyStrategy.getStartTime();
        Date newStartDate = new DateTime(oldStartDate).millisOfDay().withMinimumValue().toDate();
        collectMoneyStrategy.setStartTime(newStartDate);

        Date oldEndDate = collectMoneyStrategy.getEndTime();
        Date newEndDate = new DateTime(oldEndDate).millisOfDay().withMaximumValue().toDate();
        collectMoneyStrategy.setEndTime(newEndDate);
    }

    @Override
    public CollectMoneyStrategy updateStrategy(CollectMoneyStrategy collectMoneyStrategy, Map<String, Object> schemes) {
        CollectMoneyStrategy targetCollectMoneyStrategy = jpaBaseDao.getEntityById(CollectMoneyStrategy.class, collectMoneyStrategy.getId());
        // 保留非设置信息
        collectMoneyStrategy.setCreateTime(targetCollectMoneyStrategy.getCreateTime());
        collectMoneyStrategy.setUuid(targetCollectMoneyStrategy.getUuid());
        collectMoneyStrategy.setOrganizationId(targetCollectMoneyStrategy.getOrganizationId());
        collectMoneyStrategy.setAvailable(AvailableEnum.AVAILABLE);
        collectMoneyStrategy.setIsValid(1);

        // 删除旧对象关联的scheme
        if (targetCollectMoneyStrategy.getPointPSchemeId() != null) {
            CollectMoneyScheme targetScheme = jpaBaseDao.getEntityById(CollectMoneyScheme.class, targetCollectMoneyStrategy.getPointPSchemeId());
            if (targetScheme != null) presentPackDao.removeEntity(targetScheme);
        }
        if (targetCollectMoneyStrategy.getPointESchemeId() != null) {
            CollectMoneyScheme targetScheme = jpaBaseDao.getEntityById(CollectMoneyScheme.class, targetCollectMoneyStrategy.getPointESchemeId());
            if (targetScheme != null) jpaBaseDao.removeEntity(targetScheme);
        }

        // 持久化scheme
        CollectMoneyScheme paiCollectMoneyScheme = (CollectMoneyScheme) schemes.get("paiCollectMoneyScheme");
        CollectMoneyScheme beiCollectMoneyScheme = (CollectMoneyScheme) schemes.get("beiCollectMoneyScheme");
        if (collectMoneyStrategy.getIsDeductionPointP() == 1 && paiCollectMoneyScheme != null) {
            jpaBaseDao.persist(paiCollectMoneyScheme);
            collectMoneyStrategy.setPointPSchemeId(paiCollectMoneyScheme.getId());

        }
        if (collectMoneyStrategy.getIsDeductionPointE() == 1 && beiCollectMoneyScheme != null) {
            jpaBaseDao.persist(beiCollectMoneyScheme);
            collectMoneyStrategy.setPointESchemeId(beiCollectMoneyScheme.getId());
        }

        // 持久化strategy
        return jpaBaseDao.merge(collectMoneyStrategy);
    }

    @Override
    public CollectMoneyStrategy updateStrategyInvalid(CollectMoneyStrategy collectMoneyStrategy) {
        CollectMoneyStrategy targetStrategy = presentPackDao.getEntityById(CollectMoneyStrategy.class, collectMoneyStrategy.getId());
        collectMoneyStrategy.setUpdateTime(new Date());
        MergeUtil.merge(collectMoneyStrategy, targetStrategy);
        return presentPackDao.merge(targetStrategy);
    }

    @Override
    public CollectMoneyStrategy getById(Long collectMoneyStrategyId) {
        return presentPackDao.getEntityById(CollectMoneyStrategy.class, collectMoneyStrategyId);
    }

    @Override
    @SuppressWarnings("all")
    public CollectMoneyStrategySnapshot selectByTerminalUserId(Long terminalUserId) {
        TerminalUser terminalUser = jpaBaseDao.getEntityById(TerminalUser.class, terminalUserId);
        /**根据商家  查询出所有策略*/
        CollectMoneyStrategy strategy = this.getCurrentStrategyByOrganizationId(terminalUser.getOrganizationId());
        if (null == strategy) {
            strategy = new CollectMoneyStrategy();
            strategy.setStrategyName("无优惠");
            strategy.setOrganizationId(terminalUser.getOrganizationId());
            strategy.setIsDiscount(0);
            strategy.setIsGivingPointE(0);
            strategy.setIsGivingPointP(0);
            strategy.setStartTime(new Date());
            strategy.setEndTime(new Date());
        }


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
                case SHOPPING_AMOUNT:
                    strategySnapshot.setGivingPonitPDesc("满" + scheme.getMiniAmount() + "送" + scheme.getMiniGiveAmount() + "积分");
                    break;
                case PERCENTAGE:
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

        return strategySnapshot;
    }

    @Override
    public CollectMoneyStrategy getByDetail(Long collectMoneyStrategyId) {
        return collectMoneyStrategyDao.getEntityById(CollectMoneyStrategy.class, collectMoneyStrategyId);
    }

    @Override
    public Long insertCompute(Long terminalUserId, Long collectMoneyStrategyId, String username, Float consumptionAmount) {
        //        /**1,判断是否添加C端用户*/
        //        User user = authService.getByUsername(username);
        //        if (user == null) {
        //            user = new User();
        //            user.setUsername(username);
        //
        //            user.setPassword(username);
        //            user.setGender(GenderEnum.FEMALE);
        //            authService.register(user);
        //            smsMessageService.append(username, SmsTypeEnum.ORDER_VAL_NOREG, null);
        //        }
        //        /**3,查询C端用户积分*/
        //        List<Object[]> list = userPointDetailService.selectIntegral(user.getId());
        //        Long totalE = 0L;
        //        Long totalP = 0L;
        //        for (Object[] objList : list) {
        //            Object[] obj = objList;
        //            if ("E".equals(obj[0].toString())) {
        //                totalE = Long.valueOf(obj[1].toString());
        //            }
        //            if ("P".equals(obj[0].toString())) {
        //                totalP = Long.valueOf(obj[1].toString());
        //            }
        //        }
        //        /**4,判断是否选择策略*/
        //        if (collectMoneyStrategyId == 0 || collectMoneyStrategyId == null) {
        //            return totalE;
        //        }
        //        CollectMoneyStrategy strategy = getByDetail(collectMoneyStrategyId);
        //        /**5,判断用户 积分抵扣  是否充足*/
        //        if (strategy.getPaiDeduction() != null && strategy.getPaiDeduction() != 0.0 ||
        //                strategy.getBeiDeduction() != null && strategy.getBeiDeduction() != 0.0) {
        //
        //            if (strategy.getPaiDeduction() != null && strategy.getPaiDeduction() > totalP) {
        //                throw new BaseBusinessException(PoiError.POINT_P_NOT_ENOUGH);
        //            }
        //            if (strategy.getBeiDeduction() != null && strategy.getBeiDeduction() > totalE) {
        //                throw new BaseBusinessException(PoiError.POINT_E_NOT_ENOUGH);
        //            }
        //        }
        //
        //        return totalE;
        return null;
    }

    @Override
    public List<CollectMoneyStrategy> selectCollectMoneyStrategyList(Map<String, Object> map) {
        return presentPackDao.getEntitiesByFieldList(CollectMoneyStrategy.class, map);
    }

    @Override
    public void deleteCollectMoneyStrategy(Long collectMoneyStrategyId) {
        CollectMoneyStrategy collectMoneyStrategy = presentPackDao.getEntityById(CollectMoneyStrategy.class, collectMoneyStrategyId);
        collectMoneyStrategy.setAvailable(AvailableEnum.UNAVAILABLE);
        presentPackDao.merge(collectMoneyStrategy);
    }

    @Transactional
    @Override
    public void updateCollectMoneyStrategy() {
        collectMoneyStrategyDao.updateCollectMoneyStrategy();
    }

    @Override
    public boolean checkStrategy(Long organizationId, Date startTime, Date endTime, String executeStartTime, String executeEndTime) {
        if (organizationId != null && startTime != null && endTime != null && StringUtils.isNotBlank(executeStartTime) && StringUtils.isNotBlank(executeEndTime))
            return collectMoneyStrategyDao.checkStrategy(organizationId, startTime, endTime, executeStartTime, executeEndTime);
        else
            return false;
    }


}