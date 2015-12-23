package com.pemass.service.pemass.ec.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.dao.ec.ClearingDao;
import com.pemass.persist.domain.jpa.ec.Clearing;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.Settlement;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.domain.jpa.poi.UserConsumeDetail;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.domain.jpa.sys.Company;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.vo.OrgAndCompanyVO;
import com.pemass.persist.enumeration.ClearSourceEnum;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.SettlementRoleEnum;
import com.pemass.persist.enumeration.TransactionAccountTypeEnum;
import com.pemass.service.pemass.ec.ClearingService;
import com.pemass.service.pemass.ec.OrderService;
import com.pemass.service.pemass.ec.TransactionService;
import com.pemass.service.pemass.poi.PointPurchaseService;
import com.pemass.service.pemass.poi.UserConsumeDetailService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.sys.CompanyService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: ClearingServiceImpl
 * @Author: luoc
 * @CreateTime: 2015-08-18 10:38
 */
@Service
public class ClearingServiceImpl implements ClearingService {

    @Resource
    private BaseDao jpaBaseDao;
    @Resource
    private ClearingDao clearingDao;
    @Resource
    private OrderService orderService;
    @Resource
    private CompanyService companyService;
    @Resource
    private TransactionService transactionService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private PointPurchaseService pointPurchaseService;
    @Resource
    private UserPointDetailService userPointDetailService;
    @Resource
    private UserConsumeDetailService userConsumeDetailService;


    @Override
    public void saveClearing(Clearing clearing) {
        Preconditions.checkNotNull(clearing);
        jpaBaseDao.persist(clearing);
    }

    @Override
    public Clearing updateClearing(Clearing clearing) {
        Preconditions.checkNotNull(clearing);
        return jpaBaseDao.merge(clearing);
    }

    @Override
    public DomainPage selectAllClearingByPage(Map<String, Object> conditions, long pageIndex, long pageSize) {
        checkNotNull(conditions);
        /** 将查询条件完善成expressions，并获取查询结果 **/
        List<Expression> expressions = getClearingExpressions(conditions);
        @SuppressWarnings("unchecked")
        DomainPage<Clearing> domainPage = clearingDao.getEntitiesPagesByExpressionList(Clearing.class, expressions, "createTime", BaseDao.OrderBy.DESC, pageIndex, pageSize);

        /** 重新封装返回结果 **/
        List<Clearing> searchResult = domainPage.getDomains();
        List<Map<String, Object>> newResult = Lists.newArrayList();
        getClearNewResult(searchResult, newResult);

        /* 重新封装返回结果 */
        long totalCount = domainPage.getTotalCount();
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(newResult);

        return returnDomainPage;
    }

    private void getClearNewResult(List<Clearing> searchResult, List<Map<String, Object>> newResult) {
        for (Clearing clearing : searchResult) {
            Map<String, Object> returnMap = Maps.newHashMap();
            returnMap.put("clearing", clearing);
            /* 获取下单日期和编号 */
            Map<String, Object> dateAndIdentifier = this.getTransactionDateAndIdentifier(clearing.getClearSource(), clearing.getSourceTargetId());
            returnMap.put("date", dateAndIdentifier.get("date"));
            returnMap.put("identifier", dateAndIdentifier.get("identifier"));

            /* 出账方和入账方 */
            Map<String, Object> outgoAndIncomeName = getOutgoAndIncomeName(clearing.getSettlementRole(), clearing.getSettlementRoleTargetId());
            String outgoName = (String) outgoAndIncomeName.get("outgoName"); // 出账方
            String incomeName = (String) outgoAndIncomeName.get("incomeName"); // 入账方
            returnMap.put("disburseName", outgoName);
            returnMap.put("earningsName", incomeName);

            newResult.add(returnMap);
        }
    }

    private List<Expression> getClearingExpressions(Map<String, Object> conditions) {
        /** 获取查询条件 **/
        Integer isOutgo = (Integer) conditions.get("isOutgo"); // 出账或入账
        Integer isSettle = (Integer) conditions.get("isSettle"); // 清算状态
        String organizationStr = (String) conditions.get("organizationStr"); // 商户或者公司信息
        ClearSourceEnum clearSource = (ClearSourceEnum) conditions.get("clearSource");   //清分来源
        Date startTime = (Date) conditions.get("startTime");
        Date endTime = (Date) conditions.get("endTime");
        SettlementRoleEnum settlementRole = (SettlementRoleEnum) conditions.get("settlementRole"); // 清算类型

        /** 获取满足条件的结果 **/
        List<Expression> expressions = Lists.newArrayList();

        if (isOutgo != null) {
            Expression expression = null;
            if (isOutgo == 0) {
                expression = new Expression("targetAmount", Operation.LessThanEqual, 0f); // 商户出账targetAmount < 0
            } else if (isOutgo == 1) {
                expression = new Expression("targetAmount", Operation.GreaterThanEqual, 0f); // 商户入账targetAmount > 0
            }
            expressions.add(expression);
        }

        if (clearSource != null) {
            Expression expression = new Expression("clearSource", Operation.Equal, clearSource);
            expressions.add(expression);
        }

        if (isSettle != null) {
            Expression expression = new Expression("isSettle", Operation.Equal, isSettle);
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

        if (startTime != null && endTime != null) {
            startTime = new DateTime(startTime).millisOfDay().withMinimumValue().toDate();
            endTime = new DateTime(endTime).millisOfDay().withMaximumValue().toDate();
            Expression startExpression = new Expression("settlementDate", Operation.GreaterThanEqual, startTime);
            Expression endExpression = new Expression("settlementDate", Operation.LessThanEqual, endTime);
            expressions.add(startExpression);
            expressions.add(endExpression);
        }

        if (settlementRole != null) {
            Expression expression = new Expression("settlementRole", Operation.Equal, settlementRole);
            expressions.add(expression);
        }

        return expressions;
    }

    @Override
    public List<Map<String, Object>> selectAllClearing(Map<String, Object> conditions) {
        /** 将查询条件完善成expressions，并获取查询结果 **/
        List<Expression> expressions = getClearingExpressions(conditions);
        List<Clearing> searchResult = clearingDao.getEntitiesByExpressionList(Clearing.class, expressions);

        /** 重新封装返回结果 **/
        List<Map<String, Object>> newResult = Lists.newArrayList();
        getClearNewResult(searchResult, newResult);

        return newResult;
    }

    @Override
    public List<Settlement> getCountClearing() {
        List<Settlement> settlementList = Lists.newArrayList();

        List<Object[]> list = clearingDao.getGroupClearing();
        for (Object[] objects : list) {
            Settlement settlement = new Settlement();
            settlement.setIncomeBankCardId(Long.valueOf(objects[0].toString()));
            settlement.setOutgoBankCardId(Long.valueOf(objects[1].toString()));
            settlement.setSettlementRoleTargetId(Long.valueOf(objects[2].toString()));
            settlement.setSettlementRole(SettlementRoleEnum.valueOf(objects[3].toString()));
            settlement.setPayableAmount(Double.valueOf(objects[4].toString()));
            settlement.setProvinceId(Long.valueOf(objects[5].toString()));
            settlement.setCityId(Long.valueOf(objects[6].toString()));
            settlement.setDistrictId(Long.valueOf(objects[7].toString()));
            settlement.setTargetBankCardId(Long.valueOf(objects[8].toString()));
            settlement.setTargetAmount(Double.valueOf(objects[9].toString()));
            if (Integer.valueOf(objects[10].toString()) == 0) {
                settlement.setTransactionAccountTypeEnum(TransactionAccountTypeEnum.ORGANIZATION);
            } else {
                settlement.setTransactionAccountTypeEnum(TransactionAccountTypeEnum.COMPANY);
            }
            settlementList.add(settlement);
        }
        return settlementList;
    }

    @Override
    public List<Clearing> getClearingGroup(Map<String, Object> fieldNameValueMap) {
        return jpaBaseDao.getEntitiesByFieldList(Clearing.class, fieldNameValueMap);
    }

    @Override
    public Clearing getClearingById(Long id) {
        Preconditions.checkNotNull(id);
        return jpaBaseDao.getEntityById(Clearing.class, id);
    }

    @Override
    public DomainPage selectClearingInfoByIds(List<Long> ids, long pageIndex, long pageSize) {
        Preconditions.checkNotNull(ids);
        Expression expression = new Expression("id", Operation.IN, ids);
        DomainPage<Clearing> domainPage = jpaBaseDao.getEntitiesPagesByExpression(Clearing.class, expression, pageIndex, pageSize);
        /**重新封装返回结果**/
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        /*1.循环返回的清分表中的数据*/
        for (Clearing clearing : domainPage.getDomains()) {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("clearing", clearing);
            //获取下单日期和编号
            Map<String, Object> dateAndIdentifier = this.getTransactionDateAndIdentifier(clearing.getClearSource(), clearing.getSourceTargetId());
            returnMap.put("date", dateAndIdentifier.get("date"));
            returnMap.put("identifier", dateAndIdentifier.get("identifier"));

            Map<String, Object> outgoAndIncomeName = getOutgoAndIncomeName(clearing.getSettlementRole(), clearing.getSettlementRoleTargetId());
            returnMap.put("disburseName", outgoAndIncomeName.get("outgoName")); // 出账方
            returnMap.put("earningsName", outgoAndIncomeName.get("incomeName")); // 入账方
            result.add(returnMap);
        }
        /*2.处理返回结果*/
        long totalCount = domainPage.getTotalCount();
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, totalCount);
        returnDomainPage.getDomains().addAll(result);
        return returnDomainPage;
    }

    @Override
    public DomainPage selectPointProfitByOrganization(Long organizationId, long pageIndex, long pageSize) {
        Preconditions.checkNotNull(organizationId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("settlementRole", SettlementRoleEnum.POINT_FOR_ISSUE);
        params.put("settlementRoleTargetId", organizationId);

        DomainPage<Clearing> domainPage = jpaBaseDao.getEntitiesPagesByFieldList(Clearing.class, params, pageIndex, pageSize);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        /*1.循环返回的清分表中的数据*/
        for (Clearing clearing : domainPage.getDomains()) {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("clearing", clearing);
            //获取下单日期和E积分数量
            Order order = orderService.getById(clearing.getSourceTargetId());
            returnMap.put("date", order.getPayTime());
            ConsumeTypeEnum consumeType = null;
            if (ClearSourceEnum.PRODUCT_ORDER.toString().equals(clearing.getClearSource().toString())) {
                consumeType = ConsumeTypeEnum.ORDER;
            } else if (ClearSourceEnum.CUSTOMIZATION_ORDER.toString().equals(clearing.getClearSource().toString())) {
                consumeType = ConsumeTypeEnum.CUSTOMIZATION;
            }
            Integer pointAmount = 0;
            List<UserConsumeDetail> userConsumeDetails = userConsumeDetailService.selectByConsumeTypeAndTargetId(consumeType, order.getId());
            for (UserConsumeDetail consumeDetail : userConsumeDetails) {
                UserPointDetail userPointDetail = userPointDetailService.getById(consumeDetail.getUserPointDetailId());
                if (organizationId.equals(userPointDetail.getOrganizationId())) {
                    pointAmount = pointAmount + consumeDetail.getAmount();
                }
            }
            returnMap.put("pointAmount", pointAmount);

            result.add(returnMap);
        }
        /*2.处理返回结果*/
        long totalCount = domainPage.getTotalCount();
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, totalCount);
        returnDomainPage.getDomains().addAll(result);
        return returnDomainPage;
    }

    @Override
    @SuppressWarnings("all")
    public Map<String, Object> getOrganizationClearList(Map<String, Object> map, Long organizationId, String mark, long pageIndex, long pageSize) {
        DomainPage domainPage = clearingDao.getOrganizationClearList(map, organizationId, pageIndex, pageSize);
        List<Object[]> list = Lists.newArrayList();
        int paymentStatus = Integer.valueOf(map.get("paymentStatus").toString());
        if (domainPage != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Clearing clearing = (Clearing) domainPage.getDomains().get(i);
                Object[] objects = new Object[4];
                objects[0] = clearing;
                if (clearing.getClearSource().equals(ClearSourceEnum.POINT_PURCHASE)) {
                    objects[1] = jpaBaseDao.getEntityById(PointPurchase.class, clearing.getSourceTargetId());
                } else {
                    objects[1] = jpaBaseDao.getEntityById(Order.class, clearing.getSourceTargetId());
                }
                if (organizationId == null) organizationId = clearing.getSettlementRoleTargetId();
                Map<String, Object> clearMap = getOutgoAndIncomeName(clearing.getSettlementRole(), organizationId);
                if ((clearMap.get("outgoName").toString()).equals("积分通")) {
                    objects[2] = 0;
                } else {
                    objects[2] = 1;
                }
                list.add(objects);
            }
        }
        domainPage.setDomains(list);
        Map<String, Object> totalMap = computePaymentAmount(domainPage);
        return totalMap;
    }

    private Map computePaymentAmount(DomainPage domainPage) {
        double totalRevenue = 0.0;//总收入
        double noRevenue = 0.0;//待收入
        double totalSpending = 0.0;//总支出
        double noSpending = 0.0;//待支出
        Map<String, Object> map = Maps.newHashMap();
        if (domainPage.getDomains() != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = (Object[]) domainPage.getDomains().get(i);
                Clearing clearing = (Clearing) objects[0];
                int flag = Integer.valueOf(objects[2].toString());
                if (clearing.getIsSettle() == 2 && flag == 0) {
                    totalRevenue = ArithmeticUtil.add(totalRevenue, clearing.getAmount());
                } else if (clearing.getIsSettle() != 2 && flag == 0) {
                    noRevenue = ArithmeticUtil.add(noRevenue, clearing.getAmount());
                } else if (clearing.getIsSettle() == 2 && flag == 1) {
                    totalSpending = ArithmeticUtil.add(totalSpending, clearing.getAmount());
                } else if (clearing.getIsSettle() != 2 && flag == 1) {
                    noSpending = ArithmeticUtil.add(noSpending, clearing.getAmount());
                }
            }
        }
        map.put("domainPage", domainPage);
        map.put("noRevenue",noRevenue);
        map.put("totalRevenue",totalRevenue);
        map.put("totalSpending", totalSpending);
        map.put("noSpending", noSpending);
        return map;
    }


    /**
     * 获取清分来源的交易日期和编号
     *
     * @return 返回结果
     */
    private Map<String, Object> getTransactionDateAndIdentifier(ClearSourceEnum clearSource, Long targetId) {
        Map<String, Object> dateAndIdentifier = Maps.newHashMap();
        switch (clearSource) {
            case PRODUCT_ORDER:
            case CUSTOMIZATION_ORDER:
            case ONE_PURCHASE_ORDER:
            case CONVERT_POINT_E:
                Order order = orderService.getById(targetId);
                dateAndIdentifier.put("date", order.getPayTime());
                dateAndIdentifier.put("identifier", order.getOrderIdentifier());
                break;
            case POINT_PURCHASE:
                PointPurchase pointPurchase = pointPurchaseService.getById(targetId);
                dateAndIdentifier.put("date", pointPurchase.getCreateTime());
                dateAndIdentifier.put("identifier", pointPurchase.getPurchaseIdentifier());
                break;
        }
        return dateAndIdentifier;
    }


    public Map<String, Object> getOutgoAndIncomeName(SettlementRoleEnum settlementRole, Long settlementRoleTargetId) {
        checkNotNull(settlementRole);
        Map<String, Object> outgoAndIncomeName = Maps.newHashMap();

        Company company;
        Organization organization;
        switch (settlementRole) {
            // 积分通 -> 商户和代理商
            case CONVERT_POINT_E:
            case DISTRIBUTION_PROFIT:
            case PRODUCT_PRINCIPAL:
            case POINT_FOR_ISSUE:
            case AGENT_PROFIT_FOR_ISSUE:
            case AGENT_PROFIT_FOR_ACCEPT:
                checkNotNull(settlementRoleTargetId);
                outgoAndIncomeName.put("outgoName", "积分通");
                organization = organizationService.getOrganizationById(settlementRoleTargetId);
                outgoAndIncomeName.put("incomeName", organization.getOrganizationName());
                return outgoAndIncomeName;
            // 商户 -> 积分通
            case PRODUCT_PRICE:
            case POINT_E_PRINCIPAL:
            case POINT_P_CHARGE:
            case CALL_BACK_POINT_E:
            case SERVICE_PAY_BACK_PRICE:
                checkNotNull(settlementRoleTargetId);
                organization = organizationService.getOrganizationById(settlementRoleTargetId);
                outgoAndIncomeName.put("outgoName", organization.getOrganizationName());
                outgoAndIncomeName.put("incomeName", "积分通");
                return outgoAndIncomeName;
            // 积分通 -> 积分通
            case CASHBACK:
            case SYSTEM_CHARGE:
                outgoAndIncomeName.put("outgoName", "积分通");
                outgoAndIncomeName.put("incomeName", "积分通");
                return outgoAndIncomeName;
            // 积分通 -> 省公司
            case POINT_PROFIT_FOR_ISSUE:
            case POINT_PROFIT_FOR_ACCEPT:
                checkNotNull(settlementRoleTargetId);
                company = companyService.selectById(settlementRoleTargetId);
                outgoAndIncomeName.put("outgoName", "积分通");
                outgoAndIncomeName.put("incomeName", company.getCompanyName());
                return outgoAndIncomeName;
            // 积分通 -> 银联
            case SERVICE_PAY_PRICE:
            case SERVICE_CUT_PRICE:
                outgoAndIncomeName.put("outgoName", "积分通");
                outgoAndIncomeName.put("incomeName", "银联");
                return outgoAndIncomeName;

            default:
                throw new BaseBusinessException("未作处理的枚举类型,BY:" + settlementRole.toString());
        }
    }

    @Override
    public DomainPage getBusinessProfitsList(Map<String, Object> map, long pageIndex, long pageSize) {
        List list0 = clearingDao.getBusinessProfitsOrganizationList(0);//查询商户
        List list1 = clearingDao.getBusinessProfitsOrganizationList(1);//查询积分通公司
        if(list1!=null && list1.size()>0){//合并数据
            for(int k=0;k<list1.size();k++){
                list0.add(list1.get(k));
            }
        }
        List newList = Lists.newArrayList();
        if (list0 != null && list0.size() > 0) {
            for (int i = 0; i < list0.size(); i++) {
                Object[] newObjects = new Object[5];
                double income = 0.0;//收单
                double spending = 0.0;//发分
                Object[] objects = (Object[]) list0.get(i);
                if((objects[2].toString()).equals("POINT_PROFIT_FOR_ISSUE")
                        ||(objects[2].toString()).equals("POINT_PROFIT_FOR_ACCEPT")){
                    newObjects[4] = "省公司";
                }else if((objects[2].toString()).equals("AGENT_PROFIT_FOR_ISSUE")
                        ||(objects[2].toString()).equals("AGENT_PROFIT_FOR_ACCEPT")){
                    newObjects[4] = "代理商";
                }else if((objects[2].toString()).equals("POINT_FOR_ISSUE")){
                    newObjects[4] = "商户";
                }
                map.put("settlementRoleId", objects[0]);
                map.put("status",objects[1]);
                DomainPage domainPage = clearingDao.getBusinessProfitsList(map, 1L, Integer.MAX_VALUE);
                if (domainPage.getDomains() != null && domainPage.getDomains().size() > 0) {
                    for (int j = 0; j < domainPage.getDomains().size(); j++) {
                        DecimalFormat df = new DecimalFormat("###.####");
                        Clearing clearing = (Clearing) domainPage.getDomains().get(j);
                        if (clearing.getSettlementRole().equals(SettlementRoleEnum.POINT_PROFIT_FOR_ISSUE)
                                || clearing.getSettlementRole().equals(SettlementRoleEnum.AGENT_PROFIT_FOR_ISSUE)
                                || clearing.getSettlementRole().equals(SettlementRoleEnum.POINT_FOR_ISSUE)) {
                            spending = ArithmeticUtil.add(spending, clearing.getAmount());
                        } else if (clearing.getSettlementRole().equals(SettlementRoleEnum.POINT_PROFIT_FOR_ACCEPT)
                                || clearing.getSettlementRole().equals(SettlementRoleEnum.AGENT_PROFIT_FOR_ACCEPT)) {
                            income = ArithmeticUtil.add(income, clearing.getAmount());
                        }
                    }
                }
                long id = new BigInteger(objects[0].toString()).longValue();
                if (Integer.valueOf(objects[1].toString()) == 0) {
                    newObjects[0] = jpaBaseDao.getEntityById(Organization.class, id);
                } else {
                    newObjects[0] = jpaBaseDao.getEntityById(Company.class, id);
                }
                newObjects[1] = income;
                newObjects[2] = spending;
                newObjects[3] = objects[1];
                newList.add(newObjects);
            }

        }
        long totalCount = newList.size();
        DomainPage domainPage = new DomainPage(Integer.MAX_VALUE, 1L, totalCount);
        domainPage.setDomains(newList);
        return domainPage;
    }
}
