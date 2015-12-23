/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.ec.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.dao.ec.ClearingDao;
import com.pemass.persist.domain.jpa.ec.Clearing;
import com.pemass.persist.domain.jpa.sys.Company;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.vo.OrgAndCompanyVO;
import com.pemass.persist.domain.vo.SettlementStatisticsVO;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.SettlementRoleEnum;
import com.pemass.persist.enumeration.TransactionAccountTypeEnum;
import com.pemass.service.pemass.ec.ClearingGatherService;
import com.pemass.service.pemass.sys.CompanyService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: SupplierGatherServiceImpl
 * @Author: vahoa.ma
 * @CreateTime: 2015-09-14 14:03
 */
@Service
public class ClearingGatherServiceImpl implements ClearingGatherService {

    @Resource
    private OrganizationService organizationService;
    @Resource
    private CompanyService companyService;
    @Resource
    private ClearingDao clearingDao;


    @Override
    public DomainPage<SettlementStatisticsVO> statistics(Map<String, Object> fieldNameValueMap, long pageIndex, long pageSize) {
        /** 获取查询条件 **/
        String organizationStr = (String) fieldNameValueMap.get("organizationStr"); // 商户或者公司信息
        Integer organizationType = (Integer) fieldNameValueMap.get("organizationType");
        Date startTime = (Date) fieldNameValueMap.get("startTime");
        Date endTime = (Date) fieldNameValueMap.get("endTime");
        /** 获取满足条件的结果 **/
        List<Expression> expressions = Lists.newArrayList();

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

        if (startTime != null && endTime != null) {

            startTime = new DateTime(startTime).millisOfDay().withMinimumValue().toDate();
            endTime = new DateTime(endTime).millisOfDay().withMaximumValue().toDate();

            Expression startExpression = new Expression("settlementDate", Operation.GreaterThanEqual, startTime);
            Expression endExpression = new Expression("settlementDate", Operation.LessThanEqual, endTime);
            expressions.add(startExpression);
            expressions.add(endExpression);
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

         /* 获取结果 */
        @SuppressWarnings("unchecked")
        DomainPage<Clearing> domainPage = clearingDao.getEntitiesPagesByExpressionList(Clearing.class, expressions, pageIndex, 99999);


        Map<String, Object> statisticsMap = Maps.newHashMap();

//        /** 计算积分通的出入账 **/
//        statisticsJFT(domainPage.getDomains(), statisticsMap);

        /** 计算商户和公司到出入账 **/
        statisticsOrganization(domainPage.getDomains(), statisticsMap);

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
    private void statisticsJFT(List<Clearing> settlements, Map<String, Object> statisticsMap) {
        // 积分通出账
        List<SettlementRoleEnum> jftOutgoRoles = Lists.newArrayList(
                SettlementRoleEnum.CONVERT_POINT_E, SettlementRoleEnum.DISTRIBUTION_PROFIT,
                SettlementRoleEnum.PRODUCT_PRINCIPAL, SettlementRoleEnum.CASHBACK,
                SettlementRoleEnum.SYSTEM_CHARGE, SettlementRoleEnum.POINT_PROFIT_FOR_ISSUE,
                SettlementRoleEnum.POINT_PROFIT_FOR_ACCEPT, SettlementRoleEnum.AGENT_PROFIT_FOR_ISSUE,
                SettlementRoleEnum.AGENT_PROFIT_FOR_ACCEPT, SettlementRoleEnum.POINT_FOR_ISSUE,
                SettlementRoleEnum.SERVICE_PAY_PRICE, SettlementRoleEnum.SERVICE_CUT_PRICE);

        // 积分通入帐
        List<SettlementRoleEnum> jftIncomeRoles = Lists.newArrayList(
                SettlementRoleEnum.PRODUCT_PRICE, SettlementRoleEnum.POINT_E_PRINCIPAL,
                SettlementRoleEnum.POINT_P_CHARGE, SettlementRoleEnum.CASHBACK,
                SettlementRoleEnum.SYSTEM_CHARGE, SettlementRoleEnum.CALL_BACK_POINT_E,
                SettlementRoleEnum.SERVICE_PAY_BACK_PRICE);
        for (Clearing clearing : settlements) {
            SettlementRoleEnum settlementRole = clearing.getSettlementRole();
            Long settlementRoleTargetId = clearing.getSettlementRoleTargetId();
            String mapKey = SettlementStatisticsVO.GroupType.JFT.toString() + settlementRoleTargetId;

            /** 计算商户的出账 **/
            if (jftOutgoRoles.contains(settlementRole)) {
                SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
                if (statisticsVO != null) {
                    double outgoTotalAmount = statisticsVO.getOutgoTotalAmount() == null ? 0 : statisticsVO.getOutgoTotalAmount();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    outgoTotalAmount = Float.parseFloat(ArithmeticUtil.add(outgoTotalAmount, payableAmount).toString());
                    statisticsVO.setStatisticDate(DateTime.now().toDate());
                    statisticsVO.setOutgoTotalAmount(outgoTotalAmount);
                } else {
                    statisticsVO = new SettlementStatisticsVO();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    Organization organization = organizationService.getOrganizationById(settlementRoleTargetId);
                    statisticsVO.setOrganizationID(settlementRoleTargetId);
                    statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.JFT);
                    statisticsVO.setOrganizationName(organization.getOrganizationName());
                    statisticsVO.setOutgoTotalAmount(payableAmount);
                    statisticsVO.setStatisticDate(DateTime.now().toDate());

                    statisticsMap.put(mapKey, statisticsVO);
                }
            }

            /** 计算商户的入帐 **/
            if (jftIncomeRoles.contains(settlementRole)) {
                SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
                // 累加该商户的入帐金额
                if (statisticsVO != null) {
                    double incomeTotalAmount = statisticsVO.getIncomeTotalAmount() == null ? 0 : statisticsVO.getIncomeTotalAmount();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    incomeTotalAmount = Float.parseFloat(ArithmeticUtil.add(incomeTotalAmount, payableAmount).toString());
                    statisticsVO.setStatisticDate(DateTime.now().toDate());
                    statisticsVO.setIncomeTotalAmount(incomeTotalAmount);
                } else {
                    statisticsVO = new SettlementStatisticsVO();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    Organization organization = organizationService.getOrganizationById(settlementRoleTargetId);
                    statisticsVO.setOrganizationID(settlementRoleTargetId);
                    statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.JFT);
                    statisticsVO.setOrganizationName(organization.getOrganizationName());
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
    private void statisticsOrganization(List<Clearing> settlements, Map<String, Object> statisticsMap) {
        // 对于商户来说入帐类型 (排除积分通)
        List<SettlementRoleEnum> organizationIncomeRoles = Lists.newArrayList(
                SettlementRoleEnum.CONVERT_POINT_E, SettlementRoleEnum.DISTRIBUTION_PROFIT,
                SettlementRoleEnum.PRODUCT_PRINCIPAL, SettlementRoleEnum.POINT_FOR_ISSUE,
                SettlementRoleEnum.AGENT_PROFIT_FOR_ISSUE, SettlementRoleEnum.AGENT_PROFIT_FOR_ACCEPT);

        // 对于商户来说出账类型 (排除积分通)
        List<SettlementRoleEnum> organizationOutgoRoles = Lists.newArrayList(
                SettlementRoleEnum.PRODUCT_PRICE, SettlementRoleEnum.POINT_E_PRINCIPAL,
                SettlementRoleEnum.POINT_P_CHARGE, SettlementRoleEnum.CALL_BACK_POINT_E);

        // 对省公司来说入帐类型
        List<SettlementRoleEnum> companyIncomeRoles = Lists.newArrayList(
                SettlementRoleEnum.POINT_PROFIT_FOR_ISSUE, SettlementRoleEnum.POINT_PROFIT_FOR_ACCEPT);


        for (Clearing clearing : settlements) {
            SettlementRoleEnum settlementRole = clearing.getSettlementRole();
            Long settlementRoleTargetId = clearing.getSettlementRoleTargetId();
            /** 商户的入帐 **/
            if (organizationIncomeRoles.contains(settlementRole)) {
                String mapKey = SettlementStatisticsVO.GroupType.ORGANIZATION.toString() + settlementRoleTargetId;
                SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
                // 累加该商户的入帐金额
                if (statisticsVO != null) {
                    double incomeTotalAmount = statisticsVO.getIncomeTotalAmount() == null ? 0 : statisticsVO.getIncomeTotalAmount();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    incomeTotalAmount = Float.parseFloat(ArithmeticUtil.add(incomeTotalAmount, payableAmount).toString());
                    statisticsVO.setStatisticDate(DateTime.now().toDate());
                    statisticsVO.setIncomeTotalAmount(incomeTotalAmount);
                } else {
                    statisticsVO = new SettlementStatisticsVO();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    Organization organization = organizationService.getOrganizationById(settlementRoleTargetId);
                    statisticsVO.setOrganizationID(settlementRoleTargetId);
                    statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.ORGANIZATION);
                    statisticsVO.setOrganizationName(organization.getOrganizationName());
                    statisticsVO.setIncomeTotalAmount(payableAmount);
                    statisticsVO.setStatisticDate(DateTime.now().toDate());

                    statisticsMap.put(mapKey, statisticsVO);
                }
            }

            /** 商户的出账 **/
            if (organizationOutgoRoles.contains(settlementRole)) {
                String mapKey = SettlementStatisticsVO.GroupType.ORGANIZATION.toString() + settlementRoleTargetId;
                SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);
                // 累加商户出账
                if (statisticsVO != null) {
                    double outgoTotalAmount = statisticsVO.getOutgoTotalAmount() == null ? 0 : statisticsVO.getOutgoTotalAmount();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    outgoTotalAmount = Float.parseFloat(ArithmeticUtil.add(outgoTotalAmount, payableAmount).toString());
                    statisticsVO.setStatisticDate(DateTime.now().toDate());
                    statisticsVO.setOutgoTotalAmount(outgoTotalAmount);
                } else {
                    statisticsVO = new SettlementStatisticsVO();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    Organization organization = organizationService.getOrganizationById(settlementRoleTargetId);
                    statisticsVO.setOrganizationID(settlementRoleTargetId);
                    statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.ORGANIZATION);
                    statisticsVO.setOrganizationName(organization.getOrganizationName());
                    statisticsVO.setOutgoTotalAmount(payableAmount);
                    statisticsVO.setStatisticDate(DateTime.now().toDate());

                    statisticsMap.put(mapKey, statisticsVO);
                }
            }

            /** 公司的入帐 **/
            if (companyIncomeRoles.contains(settlementRole)) {
                String mapKey = SettlementStatisticsVO.GroupType.COMPANY.toString() + settlementRoleTargetId;
                SettlementStatisticsVO statisticsVO = (SettlementStatisticsVO) statisticsMap.get(mapKey);

                // 累加公司入帐
                if (statisticsVO != null) {
                    double incomeTotalAmount = statisticsVO.getIncomeTotalAmount() == null ? 0 : statisticsVO.getIncomeTotalAmount();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    incomeTotalAmount = Float.parseFloat(ArithmeticUtil.add(incomeTotalAmount, payableAmount).toString());
                    statisticsVO.setStatisticDate(DateTime.now().toDate());
                    statisticsVO.setIncomeTotalAmount(incomeTotalAmount);
                } else {
                    statisticsVO = new SettlementStatisticsVO();
                    double payableAmount = clearing.getAmount() == null ? 0 : clearing.getAmount();
                    Company company = companyService.selectById(settlementRoleTargetId);
                    statisticsVO.setOrganizationID(settlementRoleTargetId);
                    statisticsVO.setCompanyType(SettlementStatisticsVO.GroupType.COMPANY);
                    statisticsVO.setOrganizationName(company.getCompanyName());
                    statisticsVO.setIncomeTotalAmount(payableAmount);
                    statisticsVO.setStatisticDate(DateTime.now().toDate());

                    statisticsMap.put(mapKey, statisticsVO);
                }
            }
        }
    }
}