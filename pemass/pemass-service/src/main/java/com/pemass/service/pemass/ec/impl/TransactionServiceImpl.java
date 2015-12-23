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
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.ec.TransactionDao;
import com.pemass.persist.domain.jpa.ec.Transaction;
import com.pemass.persist.domain.vo.OrgAndCompanyVO;
import com.pemass.persist.enumeration.OperateStatusEnum;
import com.pemass.persist.enumeration.TransactionAccountTypeEnum;
import com.pemass.service.pemass.ec.TransactionService;
import com.pemass.service.pemass.sys.CompanyService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: TransactionServiceImpl
 * @Author: oliver.he
 * @CreateTime: 2015-08-21 15:09
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private TransactionDao transactionDao;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private CompanyService companyService;


    @Override
    public void insert(Transaction transaction) {
        checkNotNull(transaction);
        transactionDao.persist(transaction);
    }

    @Override
    public Transaction update(Transaction source) {
        checkNotNull(source);
        source.setUpdateTime(DateTime.now().toDate());
        Transaction target = getById(source.getId());
        MergeUtil.merge(source, target);
        return transactionDao.merge(source);
    }

    @Override
    public Transaction getById(Long transactionID) {
        checkNotNull(transactionID);
        return transactionDao.getEntityById(Transaction.class, transactionID);
    }

    @Override
    public Transaction delete(Long transactionID) {
        checkNotNull(transactionID);
        Transaction target = getById(transactionID);
        target.setUpdateTime(DateTime.now().toDate());
        target.setAvailable(AvailableEnum.UNAVAILABLE);
        return transactionDao.merge(target);
    }

    @Override
    public DomainPage<Map<String, Object>> search(Map<String, Object> conditions, DomainPage domainPage) {
        checkNotNull(domainPage);
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();
        List<Expression> expressions = getTransactionExpressions(conditions);

        @SuppressWarnings("unchecked")
        DomainPage<Transaction> searchResult = transactionDao.getEntitiesPagesByExpressionList(Transaction.class, expressions, "settlementTime", BaseDao.OrderBy.DESC, pageIndex, pageSize);
        /** 完善结果 **/
        List<Transaction> searchList = searchResult.getDomains();
        List<Map<String, Object>> newResultList = Lists.newArrayList();
        getTransactionNewResult(searchList, newResultList);

        /** 处理并返回结果 **/
        Long totalCount = searchResult.getTotalCount();
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(newResultList);

        return returnDomainPage;
    }

    private void getTransactionNewResult(List<Transaction> searchList, List<Map<String, Object>> newResultList) {
        for (Transaction transaction : searchList) {
            Map<String, Object> newSettlement = Maps.newHashMap();
            // 清算表基础信息
            newSettlement.put("transaction", transaction);

            // 获取出账方和入账方的名字(根据分润商户类型的枚举值进行判断)
            TransactionAccountTypeEnum transactionAccountType = transaction.getTransactionAccountTypeEnum();
            String orgName; // 商户或者公司名字
            if (transactionAccountType == TransactionAccountTypeEnum.COMPANY)
                orgName = companyService.selectById(transaction.getTransactionAccountTypeId()).getCompanyName();
            else
                orgName = organizationService.getOrganizationById(transaction.getTransactionAccountTypeId()).getOrganizationName();

            /* 判断出商户或公司为出账还是入账(站在商户或公司的角度应付金额 > 0,则商户或公司为出账方,否则为入账方) */
            if (transaction.getPayableAmount() > 0) {
                newSettlement.put("outgoName", orgName);
                newSettlement.put("incomeName", "积分通");
            } else {
                newSettlement.put("outgoName", "积分通");
                newSettlement.put("incomeName", orgName);
            }

            newResultList.add(newSettlement);
        }
    }

    private List<Expression> getTransactionExpressions(Map<String, Object> conditions) {
        Integer isOutgo = (Integer) conditions.get("isOutgo"); // 出账或入账
        String organizationStr = (String) conditions.get("organizationStr"); // 商户或者公司信息
        Date startTime = (Date) conditions.get("startTime");
        Date endTime = (Date) conditions.get("endTime");
        OperateStatusEnum transactionStatus = (OperateStatusEnum) conditions.get("transactionStatus");
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

        if (transactionStatus != null) {
            Expression expression = new Expression("transactionStatus", Operation.Equal, transactionStatus);
            expressions.add(expression);
        }

        // 站在商户的角度商户或者公司 payableAmount > 0 商户或公司出账, payableAmount < 0 商户或公司入账
        if (isOutgo != null) {
            Expression expression = null;
            if (isOutgo == 0) {
                expression = new Expression("payableAmount", Operation.GreaterThanEqual, 0f); // 商户出账targetAmount < 0
            } else if (isOutgo == 1) {
                expression = new Expression("payableAmount", Operation.LessThanEqual, 0f); // 商户入账targetAmount > 0
            }
            expressions.add(expression);
        }

        if (StringUtils.isNotBlank(organizationStr)) {
            List<String> orgList = Splitter.on(SystemConst.REDIS_KEY_SEPARATOR_SYMBOL)
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(organizationStr);
            Expression targetID = new Expression("transactionAccountTypeId", Operation.Equal, Long.parseLong(orgList.get(0))); // 商户或者公司的ID
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
        return expressions;
    }

    @Override
    public List<Map<String, Object>> selectAllTransaction(Map<String, Object> conditions) {
        checkNotNull(conditions);
        /** 获取结果 **/
        List<Expression> expressions = getTransactionExpressions(conditions);
        List<Transaction> searchTransactions = transactionDao.getEntitiesByExpressionList(Transaction.class, expressions);

        /** 处理返回结果 **/
        List<Map<String, Object>> newResultList = Lists.newArrayList();
        getTransactionNewResult(searchTransactions, newResultList);

        return newResultList;
    }

    /**
     * @return
     */
    @Override
    public List<Transaction> getTransactionListByDate() {
        return transactionDao.getTransactionListByDate();
    }
}