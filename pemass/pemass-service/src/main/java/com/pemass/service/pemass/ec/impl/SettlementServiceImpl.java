/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.ec.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.ec.SettlementDao;
import com.pemass.persist.domain.jpa.ec.Settlement;
import com.pemass.persist.domain.jpa.ec.Transaction;
import com.pemass.persist.domain.jpa.sys.BankCard;
import com.pemass.persist.domain.jpa.sys.Company;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.vo.OrgAndCompanyVO;
import com.pemass.persist.domain.vo.SettlementStatisticsVO;
import com.pemass.persist.enumeration.*;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.ec.ClearingService;
import com.pemass.service.pemass.ec.SettlementService;
import com.pemass.service.pemass.sys.BankCardService;
import com.pemass.service.pemass.sys.CompanyService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: 结算表Service实现
 * @Author: oliver.he
 * @CreateTime: 2015-08-21 15:07
 */
@Service
public class SettlementServiceImpl implements SettlementService {

    @Resource
    private SettlementDao settlementDao;
    @Resource
    private CompanyService companyService;
    @Resource
    private BankCardService bankCardService;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private ClearingService clearingService;
    @Resource
    private OrganizationService organizationService;

    @Override
    public void insert(Settlement settlement) {
        checkNotNull(settlement);
        settlementDao.persist(settlement);
    }

    @Override
    public Settlement update(Settlement source) {
        checkNotNull(source);
        source.setUpdateTime(DateTime.now().toDate());
        Settlement target = getById(source.getId());
        MergeUtil.merge(source, target);
        return settlementDao.merge(target);
    }

    @Override
    public Settlement getById(Long settlementID) {
        checkNotNull(settlementID);
        return settlementDao.getEntityById(Settlement.class, settlementID);
    }

    @Override
    public Settlement delete(Long settlementID) {
        checkNotNull(settlementID);
        Settlement target = getById(settlementID);
        target.setUpdateTime(DateTime.now().toDate());
        target.setAvailable(AvailableEnum.UNAVAILABLE);
        return settlementDao.merge(target);
    }

    @Override
    public DomainPage<Map<String, Object>> search(Map<String, Object> conditions, DomainPage domainPage) {
        checkNotNull(domainPage);
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();
        List<Expression> expressions = getSettlementExpressions(conditions);

        @SuppressWarnings("unchecked")
        DomainPage<Settlement> searchResult = settlementDao.getEntitiesPagesByExpressionList(Settlement.class, expressions, "createTime", BaseDao.OrderBy.DESC, pageIndex, pageSize);

        /** 完善结果 **/
        List<Settlement> searchList = searchResult.getDomains();
        List<Map<String, Object>> newResultList = Lists.newArrayList();
        getSettlementNewResult(searchList, newResultList);

        /** 处理并返回结果 **/
        Long totalCount = searchResult.getTotalCount();
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(newResultList);

        return returnDomainPage;
    }

    private void getSettlementNewResult(List<Settlement> searchList, List<Map<String, Object>> newResultList) {
        for (Settlement settlement : searchList) {
            Map<String, Object> newSettlement = Maps.newHashMap();
            // 清算表基础信息
            newSettlement.put("settlement", settlement);

            // 获取出账方和入账方的名字
            SettlementRoleEnum settlementRole = settlement.getSettlementRole();
            Long settlementRoleTargetId = settlement.getSettlementRoleTargetId();
            Map<String, Object> outgoAndIncomeName = clearingService.getOutgoAndIncomeName(settlementRole, settlementRoleTargetId);
            String outgoName = (String) outgoAndIncomeName.get("outgoName"); // 出账方
            String incomeName = (String) outgoAndIncomeName.get("incomeName"); // 入账方
            newSettlement.put("outgoName", outgoName);
            newSettlement.put("incomeName", incomeName);

            newResultList.add(newSettlement);
        }
    }

    private List<Expression> getSettlementExpressions(Map<String, Object> conditions) {
        Integer isOutgo = (Integer) conditions.get("isOutgo"); // 出账或入账
        String organizationStr = (String) conditions.get("organizationStr"); // 商户或者公司信息
        Date startTime = (Date) conditions.get("startTime");
        Date endTime = (Date) conditions.get("endTime");
        OperateStatusEnum settlementStatus = (OperateStatusEnum) conditions.get("settlementStatus");
        SettlementRoleEnum settlementRole = (SettlementRoleEnum) conditions.get("settlementRole"); // 清算类型
        /** 获取满足条件的结果 **/
        List<Expression> expressions = Lists.newArrayList();
        if (startTime != null && endTime != null) {
            startTime = new DateTime(startTime).millisOfDay().withMinimumValue().toDate();
            endTime = new DateTime(endTime).millisOfDay().withMaximumValue().toDate();
            Expression startTimeExpression = new Expression("settlementTime", Operation.GreaterThanEqual, startTime);
            Expression endTimeExpression = new Expression("settlementTime", Operation.LessThanEqual, endTime);

            expressions.add(startTimeExpression);
            expressions.add(endTimeExpression);
        }

        if (settlementStatus != null) {
            Expression expression = new Expression("settlementStatus", Operation.Equal, settlementStatus);
            expressions.add(expression);
        }

        if (isOutgo != null) {
            Expression expression = null;
            if (isOutgo == 0) {
                expression = new Expression("targetAmount", Operation.LessThanEqual, 0f); // 商户出账targetAmount < 0
            } else if (isOutgo == 1) {
                expression = new Expression("targetAmount", Operation.GreaterThanEqual, 0f); // 商户入账targetAmount > 0
            }
            expressions.add(expression);
        }

        if (StringUtils.isNotBlank(organizationStr)) {
            List<String> orgList = Splitter.on(SystemConst.REDIS_KEY_SEPARATOR_SYMBOL)
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(organizationStr);
            Expression targetID = new Expression("settlementRoleTargetId", Operation.Equal, Long.parseLong(orgList.get(0))); // 商户或者公司的ID
            Expression accountType = null; // 商户或者公司类型
            // 商户Organization或者公司Company
            OrgAndCompanyVO.GroupType groupType = OrgAndCompanyVO.GroupType.valueOf(orgList.get(1));
            if (OrgAndCompanyVO.GroupType.ORGANIZATION.equals(groupType))
                accountType = new Expression("transactionAccountTypeEnum", Operation.Equal, TransactionAccountTypeEnum.ORGANIZATION);
            else if (OrgAndCompanyVO.GroupType.COMPANY.equals(groupType))
                accountType = new Expression("transactionAccountTypeEnum", Operation.Equal, TransactionAccountTypeEnum.COMPANY);

            expressions.add(accountType);
            expressions.add(targetID);
        }

        if (settlementRole != null) {
            Expression expression = new Expression("settlementRole", Operation.Equal, settlementRole);
            expressions.add(expression);
        }

        return expressions;
    }

    @Override
    public List<Map<String, Object>> selectAllSettlement(Map<String, Object> conditions) {
        checkNotNull(conditions);
        /** 获取满足条件的全部结果 **/
        List<Expression> expressions = getSettlementExpressions(conditions);
        List<Settlement> searchSettlements = settlementDao.getEntitiesByExpressionList(Settlement.class, expressions);

        /** 完善获取的结果并返回 **/
        List<Map<String, Object>> newSettlements = Lists.newArrayList();
        getSettlementNewResult(searchSettlements, newSettlements);
        return newSettlements;
    }

    @Override
    public DomainPage<SettlementStatisticsVO> statistics(Map<String, Object> conditions) {
        checkNotNull(conditions);
        Date endTime = (Date) conditions.get("endTime");
        Date startTime = (Date) conditions.get("startTime");
        Integer organizationType = (Integer) conditions.get("organizationType");
        String organizationStr = (String) conditions.get("organizationStr"); // 商户或者公司信息

        /** 拼查询条件 **/
        List<Expression> expressions = Lists.newArrayList();
        if (startTime != null && endTime != null) {
            startTime = new DateTime(startTime).millisOfDay().withMinimumValue().toDate();
            endTime = new DateTime(endTime).millisOfDay().withMaximumValue().toDate();
            Expression startTimeExpression = new Expression("settlementTime", Operation.GreaterThanEqual, startTime);
            Expression endTimeExpression = new Expression("settlementTime", Operation.LessThanEqual, endTime);

            expressions.add(startTimeExpression);
            expressions.add(endTimeExpression);
        }

        if (StringUtils.isNotBlank(organizationStr)) {
            List<String> orgList = Splitter.on(SystemConst.REDIS_KEY_SEPARATOR_SYMBOL)
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(organizationStr);
            Expression targetID = new Expression("settlementRoleTargetId", Operation.Equal, Long.parseLong(orgList.get(0))); // 商户或者公司的ID
            Expression accountType = null; // 商户或者公司类型
            // 商户Organization或者公司Company
            OrgAndCompanyVO.GroupType groupType = OrgAndCompanyVO.GroupType.valueOf(orgList.get(1));
            if (OrgAndCompanyVO.GroupType.ORGANIZATION.equals(groupType))
                accountType = new Expression("transactionAccountTypeEnum", Operation.Equal, TransactionAccountTypeEnum.ORGANIZATION);
            else if (OrgAndCompanyVO.GroupType.COMPANY.equals(groupType))
                accountType = new Expression("transactionAccountTypeEnum", Operation.Equal, TransactionAccountTypeEnum.COMPANY);

            expressions.add(accountType);
            expressions.add(targetID);
        }

        if (organizationType != null) {
            if (organizationType == 0) {
                List<Long> organizationList = organizationService.getOrganizationIDByAccountRole(AccountRoleEnum.ROLE_SUPPLIER);
                Expression transaction = new Expression("transactionAccountTypeEnum", Operation.Equal, TransactionAccountTypeEnum.ORGANIZATION);
                Expression organizations = new Expression("settlementRoleTargetId", Operation.IN, organizationList);

                expressions.add(transaction);
                expressions.add(organizations);
            } else if (organizationType == 1) {
                List<Long> organizationList = organizationService.getOrganizationIDByAccountRole(AccountRoleEnum.ROLE_CHANNEL);
                Expression transaction = new Expression("transactionAccountTypeEnum", Operation.Equal, TransactionAccountTypeEnum.ORGANIZATION);
                Expression organizations = new Expression("settlementRoleTargetId", Operation.IN, organizationList);

                expressions.add(transaction);
                expressions.add(organizations);
            } else if (organizationType == 2) {
                Expression transaction = new Expression("transactionAccountTypeEnum", Operation.Equal, TransactionAccountTypeEnum.COMPANY);

                expressions.add(transaction);
            }
        }

        /** 获取满足条件的所有的结果集**/
        List<Settlement> settlements = settlementDao.getEntitiesByExpressionList(Settlement.class, expressions);

        /** 对于返回结果的统计 **/
        Map<String, Object> statisticsMap = Maps.newHashMap();

        /** 计算积分通的出入账 **/
        statisticsJFT(settlements, statisticsMap);

        /** 计算商户和公司到出入账 **/
        statisticsOrganization(settlements, statisticsMap);

        /** 封装和返回结果 **/
        List<SettlementStatisticsVO> statisticsVOs = Lists.newArrayList();
        Set<Map.Entry<String, Object>> entrySet = statisticsMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) statisticsVOs.add((SettlementStatisticsVO) entry.getValue());

        long totalCount = statisticsVOs.size();
        DomainPage<SettlementStatisticsVO> returnDomainPage = new DomainPage<SettlementStatisticsVO>(500, 1, totalCount);
        returnDomainPage.setDomains(statisticsVOs);

        return returnDomainPage;
    }

    /**
     * 计算积分通到出账和入帐
     *
     * @param settlements   待处理集合
     * @param statisticsMap 存储处理后数据的Map
     */
    private void statisticsJFT(List<Settlement> settlements, Map<String, Object> statisticsMap) {
        // 积分通总公司出账
        List<SettlementRoleEnum> jftOutgoRoles = Lists.newArrayList(
                SettlementRoleEnum.CONVERT_POINT_E, SettlementRoleEnum.POINT_PROFIT_FOR_ISSUE,
                SettlementRoleEnum.PRODUCT_PRINCIPAL, SettlementRoleEnum.DISTRIBUTION_PROFIT,
                SettlementRoleEnum.POINT_PROFIT_FOR_ACCEPT, SettlementRoleEnum.AGENT_PROFIT_FOR_ISSUE,
                SettlementRoleEnum.AGENT_PROFIT_FOR_ACCEPT, SettlementRoleEnum.POINT_FOR_ISSUE,
                SettlementRoleEnum.SERVICE_PAY_PRICE, SettlementRoleEnum.SERVICE_CUT_PRICE,
                SettlementRoleEnum.CASHBACK);   //  CASHBACK 积分通总公司返现

        // 积分通总公司入帐
        List<SettlementRoleEnum> jftIncomeRoles = Lists.newArrayList(
                SettlementRoleEnum.PRODUCT_PRICE, SettlementRoleEnum.POINT_E_PRINCIPAL,
                SettlementRoleEnum.POINT_P_CHARGE, SettlementRoleEnum.SERVICE_PAY_BACK_PRICE,
                SettlementRoleEnum.CALL_BACK_POINT_E,
                SettlementRoleEnum.SYSTEM_CHARGE);  //  SYSTEM_CHARGE 积分通分公司平台服务费

        for (Settlement settlement : settlements) {
            SettlementRoleEnum settlementRole = settlement.getSettlementRole();

            // 积分通特殊处理
            String mapKey = SettlementStatisticsVO.GroupType.JFT.toString();

            /** 计算JFT的出账 **/
            if (jftOutgoRoles.contains(settlementRole)) {
                SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
                if (statisticsVO != null) {
                    double outgoTotalAmount = statisticsVO.getOutgoTotalAmount() == null ? 0 : statisticsVO.getOutgoTotalAmount();
                    double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
                    outgoTotalAmount = Float.parseFloat(ArithmeticUtil.add(outgoTotalAmount, payableAmount).toString());
                    statisticsVO.setStatisticDate(DateTime.now().toDate());
                    statisticsVO.setOutgoTotalAmount(outgoTotalAmount);
                } else {
                    statisticsVO = new SettlementStatisticsVO();
                    double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();

                    statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.JFT);
                    statisticsVO.setOrganizationName("积分通总公司");
                    statisticsVO.setIncomeTotalAmount(payableAmount);
                    statisticsVO.setStatisticDate(DateTime.now().toDate());

                    statisticsMap.put(mapKey, statisticsVO);
                }
            }

            /** 计算JFT的入帐 **/
            if (jftIncomeRoles.contains(settlementRole)) {
                SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
                // 累加该商户的入帐金额
                if (statisticsVO != null) {
                    double incomeTotalAmount = statisticsVO.getIncomeTotalAmount() == null ? 0 : statisticsVO.getIncomeTotalAmount();
                    double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
                    incomeTotalAmount = Float.parseFloat(ArithmeticUtil.add(incomeTotalAmount, payableAmount).toString());
                    statisticsVO.setStatisticDate(DateTime.now().toDate());
                    statisticsVO.setOutgoTotalAmount(incomeTotalAmount);
                } else {
                    statisticsVO = new SettlementStatisticsVO();
                    double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();

                    statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.JFT);
                    statisticsVO.setOrganizationName("积分通总公司");
                    statisticsVO.setIncomeTotalAmount(payableAmount);
                    statisticsVO.setStatisticDate(DateTime.now().toDate());

                    statisticsMap.put(mapKey, statisticsVO);
                }
            }

        }
    }

    /**
     * 计算商户和公司到出入账
     *
     * @param settlements   待处理集合
     * @param statisticsMap 存储处理后数据后的Map
     */
    private void statisticsOrganization(List<Settlement> settlements, Map<String, Object> statisticsMap) {
        // 对于商户来说入帐类型 (排除积分通)
        List<SettlementRoleEnum> organizationIncomeRoles = Lists.newArrayList(
                SettlementRoleEnum.CONVERT_POINT_E, SettlementRoleEnum.DISTRIBUTION_PROFIT,
                SettlementRoleEnum.PRODUCT_PRINCIPAL, SettlementRoleEnum.POINT_FOR_ISSUE,
                SettlementRoleEnum.AGENT_PROFIT_FOR_ISSUE, SettlementRoleEnum.AGENT_PROFIT_FOR_ACCEPT);

        // 对于商户来说出账类型 (排除积分通)
        List<SettlementRoleEnum> organizationOutgoRoles = Lists.newArrayList(
                SettlementRoleEnum.PRODUCT_PRICE, SettlementRoleEnum.POINT_E_PRINCIPAL,
                SettlementRoleEnum.POINT_P_CHARGE, SettlementRoleEnum.CALL_BACK_POINT_E,
                SettlementRoleEnum.SERVICE_PAY_BACK_PRICE);

        // 对省公司来说入帐类型
        List<SettlementRoleEnum> companyIncomeRoles = Lists.newArrayList(
                SettlementRoleEnum.POINT_PROFIT_FOR_ISSUE, SettlementRoleEnum.POINT_PROFIT_FOR_ACCEPT);

        // 对于银联来说入账类型
        List<SettlementRoleEnum> taelIncomeRoles = Lists.newArrayList(
                SettlementRoleEnum.SERVICE_PAY_PRICE, SettlementRoleEnum.SERVICE_CUT_PRICE);


        for (Settlement settlement : settlements) {
            SettlementRoleEnum settlementRole = settlement.getSettlementRole();
            Long settlementRoleTargetId = settlement.getSettlementRoleTargetId();
            /** 商户的入帐 **/
            if (organizationIncomeRoles.contains(settlementRole)) {
                countOrganizationIncomeAmount(statisticsMap, settlement, settlementRoleTargetId);
            }

            /** 商户的出账 **/
            if (organizationOutgoRoles.contains(settlementRole)) {
                countOrganizationOutgoAmount(statisticsMap, settlement, settlementRoleTargetId);
            }

            /** 公司的入帐 **/
            if (companyIncomeRoles.contains(settlementRole)) {
                countCompanyOutgoAmount(statisticsMap, settlement, settlementRoleTargetId);
            }

            /** 银联入账 **/
            if (taelIncomeRoles.contains(settlementRole)) {
                countTaelIncomeAmount(statisticsMap, settlement, settlementRoleTargetId);
            }
        }
    }

    /**
     * 统计公司入账
     *
     * @param statisticsMap          最终数据容器
     * @param settlement             原始数据
     * @param settlementRoleTargetId targetID
     */
    private void countOrganizationIncomeAmount(Map<String, Object> statisticsMap, Settlement settlement, Long settlementRoleTargetId) {
        String mapKey = SettlementStatisticsVO.GroupType.ORGANIZATION.toString() + settlementRoleTargetId;
        SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
        // 累加该商户的入帐金额
        if (statisticsVO != null) {
            double incomeTotalAmount = statisticsVO.getIncomeTotalAmount() == null ? 0 : statisticsVO.getIncomeTotalAmount();
            double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
            incomeTotalAmount = Float.parseFloat(ArithmeticUtil.add(incomeTotalAmount, payableAmount).toString());
            statisticsVO.setStatisticDate(DateTime.now().toDate());
            statisticsVO.setIncomeTotalAmount(incomeTotalAmount);
        } else {
            statisticsVO = new SettlementStatisticsVO();
            double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
            Organization organization = organizationService.getOrganizationById(settlementRoleTargetId);
            statisticsVO.setOrganizationID(settlementRoleTargetId);
            statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.ORGANIZATION);
            statisticsVO.setOrganizationName(organization.getOrganizationName());
            statisticsVO.setIncomeTotalAmount(payableAmount);
            statisticsVO.setStatisticDate(DateTime.now().toDate());

            statisticsMap.put(mapKey, statisticsVO);
        }
    }

    /**
     * 统计商户出帐
     *
     * @param statisticsMap          最终数据容器
     * @param settlement             原始数据
     * @param settlementRoleTargetId targetID
     */
    private void countOrganizationOutgoAmount(Map<String, Object> statisticsMap, Settlement settlement, Long settlementRoleTargetId) {
        String mapKey = SettlementStatisticsVO.GroupType.ORGANIZATION.toString() + settlementRoleTargetId;
        SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
        // 累加商户出账
        if (statisticsVO != null) {
            double outgoTotalAmount = statisticsVO.getOutgoTotalAmount() == null ? 0 : statisticsVO.getOutgoTotalAmount();
            double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
            outgoTotalAmount = Float.parseFloat(ArithmeticUtil.add(outgoTotalAmount, payableAmount).toString());
            statisticsVO.setStatisticDate(DateTime.now().toDate());
            statisticsVO.setOutgoTotalAmount(outgoTotalAmount);
        } else {
            statisticsVO = new SettlementStatisticsVO();
            double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
            Organization organization = organizationService.getOrganizationById(settlementRoleTargetId);
            statisticsVO.setOrganizationID(settlementRoleTargetId);
            statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.ORGANIZATION);
            statisticsVO.setOrganizationName(organization.getOrganizationName());
            statisticsVO.setOutgoTotalAmount(payableAmount);
            statisticsVO.setStatisticDate(DateTime.now().toDate());

            statisticsMap.put(mapKey, statisticsVO);
        }
    }

    /**
     * 统计公司的出帐
     *
     * @param statisticsMap          最终数据容器
     * @param settlement             原始数据
     * @param settlementRoleTargetId targetID
     */
    private void countCompanyOutgoAmount(Map<String, Object> statisticsMap, Settlement settlement, Long settlementRoleTargetId) {
        String mapKey = SettlementStatisticsVO.GroupType.COMPANY.toString() + settlementRoleTargetId;
        SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);

        // 累加公司入帐
        if (statisticsVO != null) {
            double outgoTotalAmount = statisticsVO.getOutgoTotalAmount() == null ? 0 : statisticsVO.getOutgoTotalAmount();
            double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
            outgoTotalAmount = Float.parseFloat(ArithmeticUtil.add(outgoTotalAmount, payableAmount).toString());
            statisticsVO.setStatisticDate(DateTime.now().toDate());
            statisticsVO.setOutgoTotalAmount(outgoTotalAmount);
        } else {
            statisticsVO = new SettlementStatisticsVO();
            double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
            Company company = companyService.selectById(settlementRoleTargetId);
            statisticsVO.setOrganizationID(settlementRoleTargetId);
            statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.COMPANY);
            statisticsVO.setOrganizationName(company.getCompanyName());
            statisticsVO.setOutgoTotalAmount(payableAmount);
            statisticsVO.setStatisticDate(DateTime.now().toDate());

            statisticsMap.put(mapKey, statisticsVO);
        }
    }

    /**
     * 统计银联入账信息
     *
     * @param statisticsMap          最终数据容器
     * @param settlement             原始数据
     * @param settlementRoleTargetId targetID
     */
    private void countTaelIncomeAmount(Map<String, Object> statisticsMap, Settlement settlement, Long settlementRoleTargetId) {
        String mapKey = "银联";
        SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
        // 累加该商户的入帐金额
        if (statisticsVO != null) {
            double incomeTotalAmount = statisticsVO.getIncomeTotalAmount() == null ? 0 : statisticsVO.getIncomeTotalAmount();
            double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
            incomeTotalAmount = Float.parseFloat(ArithmeticUtil.add(incomeTotalAmount, payableAmount).toString());
            statisticsVO.setStatisticDate(DateTime.now().toDate());
            statisticsVO.setIncomeTotalAmount(incomeTotalAmount);
        } else {
            statisticsVO = new SettlementStatisticsVO();
            double payableAmount = settlement.getPayableAmount() == null ? 0 : settlement.getPayableAmount();
            statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.CHINAPAY);
            statisticsVO.setOrganizationName("银联");
            statisticsVO.setIncomeTotalAmount(payableAmount);
            statisticsVO.setStatisticDate(DateTime.now().toDate());

            statisticsMap.put(mapKey, statisticsVO);
        }
    }

    public DomainPage selectSettlementInfoByIds(List<Long> ids, long pageIndex, long pageSize) {
        Preconditions.checkNotNull(ids);

        /*查询结果*/
        Expression expression = new Expression("id", Operation.IN, ids);
        DomainPage<Settlement> searchResult = settlementDao.getEntitiesPagesByExpression(Settlement.class, expression, pageIndex, pageSize);
        /** 完善结果 **/
        List<Settlement> searchList = searchResult.getDomains();
        List<Map<String, Object>> newResultList = Lists.newArrayList();
        for (Settlement settlement : searchList) {
            Map<String, Object> newSettlement = Maps.newHashMap();
            // 清算表基础信息
            newSettlement.put("settlement", settlement);

            // 获取出账方和入账方的名字
            SettlementRoleEnum settlementRole = settlement.getSettlementRole();
            Long settlementRoleTargetId = settlement.getSettlementRoleTargetId();
            Map<String, Object> outgoAndIncomeName = clearingService.getOutgoAndIncomeName(settlementRole, settlementRoleTargetId);
            newSettlement.put("outgoName", outgoAndIncomeName.get("outgoName"));
            newSettlement.put("incomeName", outgoAndIncomeName.get("incomeName"));

            newResultList.add(newSettlement);
        }

        /** 处理并返回结果 **/
        Long totalCount = searchResult.getTotalCount();
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(newResultList);

        return returnDomainPage;
    }

    @Override
    public List<Transaction> getGroupSettlementToTransaction() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        List<Object[]> objects = settlementDao.getGroupSettlementToTransaction();
        for (Object[] object : objects) {
            Transaction transaction = new Transaction();
            Double payableAmount = Double.valueOf(object[0].toString());
            transaction.setPayableAmount(payableAmount);
            transaction.setTargetBankCardId(Long.valueOf(object[1].toString()));
            Company totalCompany = companyService.getGroupCompanyInfo();//积分通总公司信息
            //积分总公司账户
            BankCard totalBankCard = bankCardService.getBankCardByTargetUUID(totalCompany.getUuid());
            if (payableAmount > 0) {
                transaction.setIncomeBankCardId(Long.valueOf(object[1].toString()));
                transaction.setOutgoBankCardId(totalBankCard.getId());
            } else {
                transaction.setOutgoBankCardId(Long.valueOf(object[1].toString()));
                transaction.setIncomeBankCardId(totalBankCard.getId());
            }
            transaction.setTransactionAccountTypeId(Long.valueOf(object[4].toString()));
            if (Integer.valueOf(object[5].toString()) == 0) {
                transaction.setTransactionAccountTypeEnum(TransactionAccountTypeEnum.ORGANIZATION);
            } else {
                transaction.setTransactionAccountTypeEnum(TransactionAccountTypeEnum.COMPANY);
            }
            transaction.setTransactionStatus(OperateStatusEnum.NONE);
            String identifier = sequenceService.obtainSequence(SequenceEnum.TRANSACTION);
            transaction.setTransactionIdentifier(identifier);
            transactionList.add(transaction);
        }
        return transactionList;
    }

    @Override
    public List<Settlement> getSettlementListByMap(Map<String, Object> fieldNameValueMap) {
        return settlementDao.getEntitiesByFieldList(Settlement.class, fieldNameValueMap);
    }
    @Override
    public List<Settlement> getSettlementListByCount(Long targetBankCardId) {
        return settlementDao.getSettlementListByCount(targetBankCardId);
    }
}