/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.dao.sys.AccountDao;
import com.pemass.persist.dao.sys.OrganizationDao;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.poi.PointPoolOrganization;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.Company;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PositionRoleEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.bas.ProvinceService;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.poi.PointPoolOrganizationService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @Description: OrganizationServiceImpl
 * @Author: zh
 * @CreateTime: 2014-10-13 13:27
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private AccountDao accountDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private OrganizationDao organizationDao;

    @Resource
    private ProvinceService provinceService;

    @Resource
    private SequenceService sequenceService;

    @Resource
    private PointPoolOrganizationService pointPoolOrganizationService;

    @Override
    public Organization getOrganizationById(Long orgId) {
        checkNotNull(orgId);
        return jpaBaseDao.getEntityById(Organization.class, orgId);
    }

    @Override
    public Organization updateOrganization(Organization source) {
        checkNotNull(source);
        Organization target = getOrganizationById(source.getId());
        MergeUtil.merge(source, target);
        return jpaBaseDao.merge(target);
    }


    @Override
    @Transactional
    public Organization updateAfterCreateConnection(Organization source, String pointPoolIdsStr) {
        updateOrganization(source);
        if (StringUtils.isNotBlank(pointPoolIdsStr))
            createOrganizationPointPoolConnection(pointPoolIdsStr, source.getId());
        return source;
    }

    @Override
    public DomainPage getOrganizationsByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions,
                                                   Map<String, List<Object>> collectionConditions, long pageIndex, long pageSize) {
        DomainPage domainPage = presentPackDao.getEntitiesPagesByFieldList(Organization.class, conditions, fuzzyConditions, collectionConditions, null, null, pageIndex, pageSize);
        @SuppressWarnings("unchecked") List<Organization> oldOrganizations = domainPage.getDomains();
        List<Map<String, Object>> newOrganizations = Lists.newArrayList();
        for (Organization organization : oldOrganizations) {
            Map<String, Object> newOrganization = Maps.newHashMap();
            Province province = provinceService.getProvinceByID(organization.getProvinceId());
            newOrganization.put("organization", organization);
            newOrganization.put("province", province);
            newOrganizations.add(newOrganization);
        }
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, domainPage.getTotalCount());
        returnDomainPage.setDomains(newOrganizations);
        return returnDomainPage;
    }


    private Organization getOrganization(Organization organization) {
        if (organization == null) return null;
        Organization targetOrganization = jpaBaseDao.getEntityById(Organization.class, organization.getId());
        organization.setPointRatio(organization.getPointRatio() / 100);
//        organization.setMinCashbackRatio(organization.getMinCashbackRatio() / 100);
        targetOrganization = (Organization) MergeUtil.merge(organization, targetOrganization);
        return targetOrganization;
    }

    @Override
    public Organization updateOrganization(String[] str, Organization organization) {
        Organization targetOrganization = getOrganization(organization);
        String id = "";
        if (str != null) {
            for (int i = 0; i < str.length; i++) {
                id = id + str[i] + ":";
            }
        }
        createOrganizationPointPoolConnection(id, organization.getId());
        return jpaBaseDao.merge(targetOrganization);
    }

    @Override
    public DomainPage<Map<String, Object>> getPagesByConditions(Map<String, Object> condition, long pageIndex, long pageSize) {
        checkNotNull(condition);
        Long provinceId = (Long) condition.get("provinceId");
        String organizationName = (String) condition.get("organizationName");
        AuditStatusEnum auditStatus = (AuditStatusEnum) condition.get("auditStatus");
        AccountRoleEnum accountRole = (AccountRoleEnum) condition.get("accountRole");
        @SuppressWarnings("unchecked") List<Long> provinceIds = (List<Long>) condition.get("provinceIds");

        List<Expression> expressions = Lists.newArrayList();
        if (provinceId != null) {
            Expression expression = new Expression("provinceId", Operation.Equal, provinceId);
            expressions.add(expression);
        }

        if (StringUtils.isNotBlank(organizationName)) {
            Expression expression = new Expression("organizationName", Operation.AllLike, "%" + organizationName + "%");
            expressions.add(expression);
        }

        if (auditStatus != null) {
            Expression expression = new Expression("auditStatus", Operation.Equal, auditStatus);
            expressions.add(expression);
        }

        if (accountRole != null) {
            Expression expression = new Expression("accountRole", Operation.Equal, accountRole);
            expressions.add(expression);
        }

        if (provinceId != null) {
            Expression expression = new Expression("provinceId", Operation.IN, provinceIds);
            expressions.add(expression);
        }
        DomainPage<Organization> searchDomainPage = organizationDao.getEntitiesPagesByExpressionList(Organization.class, expressions, pageIndex, pageSize);

        // 封装数据
        @SuppressWarnings("unchecked") List<Organization> oldOrganizations = searchDomainPage.getDomains();
        List<Map<String, Object>> newOrganizations = Lists.newArrayList();
        for (Organization organization : oldOrganizations) {
            Map<String, Object> newOrganization = Maps.newHashMap();
            Province province = provinceService.getProvinceByID(organization.getProvinceId());
            newOrganization.put("organization", organization);
            newOrganization.put("province", province);

            newOrganizations.add(newOrganization);
        }
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, searchDomainPage.getTotalCount());
        returnDomainPage.setDomains(newOrganizations);
        return returnDomainPage;
    }

    @Override
    public DomainPage<Map<String, Object>> getOnePointAccredit(Map<String, Object> conditions, DomainPage<Organization> domainPage) {
        checkNotNull(conditions);
        checkNotNull(domainPage);

        AuditStatusEnum auditStatus = (AuditStatusEnum) conditions.get("auditStatus");
        AuditStatusEnum oneAuditStatus = (AuditStatusEnum) conditions.get("oneAuditStatus");
        String organizationName = (String) conditions.get("organizationName");
        Long provinceId = (Long) conditions.get("provinceId");
        List<Expression> expressions = Lists.newArrayList();

        if (auditStatus != null) {
            Expression expression = new Expression("auditStatus", Operation.Equal, auditStatus);
            expressions.add(expression);
        }

        if (oneAuditStatus != null) {
            Expression expression = new Expression("oneAuditStatus", Operation.Equal, oneAuditStatus);
            expressions.add(expression);
        } else {
            List<AuditStatusEnum> oneAuditStatues = Lists.newArrayList(AuditStatusEnum.ING_AUDIT, AuditStatusEnum.HAS_AUDIT, AuditStatusEnum.FAIL_AUDIT);
            Expression expression = new Expression("oneAuditStatus", Operation.IN, oneAuditStatues);
            expressions.add(expression);
        }

        if (provinceId != null) {
            Expression expression = new Expression("provinceId", Operation.Equal, provinceId);
            expressions.add(expression);
        }

        if (StringUtils.isNotBlank(organizationName)) {
            Expression expression = new Expression("organizationName", Operation.AllLike, "%" + organizationName + "%");
            expressions.add(expression);
        }

        // 获取返回结果
        DomainPage<Organization> searchDomainPage = organizationDao.getEntitiesPagesByExpressionList(Organization.class, expressions, domainPage.getPageIndex(), domainPage.getPageSize());
        // 封装返回结果
        List<Organization> oldOrganizations = searchDomainPage.getDomains();
        List<Map<String, Object>> newOrganizations = Lists.newArrayList();
        for (Organization organization : oldOrganizations) {
            Province province = provinceService.getProvinceByID(organization.getProvinceId());
            Map<String, Object> newOrganization = Maps.newHashMap();
            newOrganization.put("organization", organization);
            newOrganization.put("province", province);

            newOrganizations.add(newOrganization);
        }

        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(searchDomainPage.getPageSize(), searchDomainPage.getPageIndex(), searchDomainPage.getTotalCount());
        returnDomainPage.setDomains(newOrganizations);
        return returnDomainPage;
    }

    @Override
    public List<Organization> getOrganizationByIds(String ids) {
        //将ids转换成List<Long>方便in使用
        List<String> idList = Splitter.on(SystemConst.SEPARATOR_SYMBOL)
                .trimResults()
                .omitEmptyStrings()
                .splitToList(ids);
        List<Long> idLongList = Lists.newArrayList();
        for (String id : idList) {
            idLongList.add(Long.parseLong(id));
        }
        //查询数据
        Expression expression = new Expression("id", Operation.IN, idLongList);
        return jpaBaseDao.getEntitiesByExpression(Organization.class, expression);
    }

    @Override
    public Object getOrganizationByCondition(String pushType, Company company, String accountRoles, String cityIds, String organizationManagerNames) {
        //前提条件
        Map<String, List<Object>> collectionConditions = getCompanyManagerProvince(company);

        //其他条件
        if ("ALL_ORGANIZATION".equals(pushType)) {
            //这里默认查询出前500商户，后期变更在做修改

            return presentPackDao.getEntitiesPagesByFieldList(Organization.class, null, null, collectionConditions, null, null, 1, 500).getDomains();
        } else if ("AREA_ORGANIZATION".equals(pushType)) {
            String[] cityArray = cityIds.split(",");
            List<Object> cityIdList = new ArrayList<Object>();
            if (cityArray.length > 0) {
                for (String cityId : cityArray) {
                    cityIdList.add("'" + cityId + "'");
                }
            }
            collectionConditions.put("cityId", cityIdList);

            //这里默认查询出前500商户，后期变更在做修改
            return presentPackDao.getEntitiesPagesByFieldList(Organization.class, null, null, collectionConditions, null, null, 1, 500).getDomains();
        } else if ("ROLE_ORGANIZATION".equals(pushType)) {
            String[] accountRoleArray = accountRoles.split(",");
            List<Object> accountRoleList = new ArrayList<Object>();
            if (accountRoleArray.length > 0) {
                for (String accountRole : accountRoleArray) {
                    accountRoleList.add("'" + accountRole + "'");
                }
            }
            collectionConditions.put("accountRole", accountRoleList);

            //这里默认查询出前500商户，后期变更在做修改
            return presentPackDao.getEntitiesPagesByFieldList(Organization.class, null, null, collectionConditions, null, null, 1, 500).getDomains();
        } else if ("ACCOUNT_ORGANIZATION".equals(pushType)) {
            return checkOrganizationByOrganizationManagerName(collectionConditions, organizationManagerNames);
        }
        return null;
    }

    @Override
    public DomainPage<Organization> getOrganizationByCondition(Map<String, Object> conditions, DomainPage domainPage) {
        return presentPackDao.getEntitiesPagesByFieldList(Organization.class, conditions, domainPage.getPageIndex(), domainPage.getPageSize());
    }

    @Override
    public Organization insertOrganization(Organization organization) {
        /**生成商户编号并赋值*/
        String identifier = sequenceService.obtainSequence(SequenceEnum.ORGANIZATION);
        organization.setOrganizationIdentifier(identifier);
        /**生成盐值并赋值*/
        String salt = UUIDUtil.randomNumber(8);
        organization.setSalt(salt);

        jpaBaseDao.persist(organization);
        return organization;
    }

    @Override
    public List<Organization> getLists() {
        return jpaBaseDao.getAllEntities(Organization.class);
    }

    @Override
    public Organization getEntityByField(String fieldName, Object fieldValue) {
        return jpaBaseDao.getEntityByField(Organization.class, fieldName, fieldValue);
    }

    @Override
    public boolean hasBeing(String organizationName) {
        Organization organization = jpaBaseDao.getEntityByField(Organization.class, "organizationName", organizationName);
        return organization == null;
    }


    @Override
    public List<Organization> getOrganizationByNoIds(Long terminalUserId, Long provinceId) {
        TerminalUser terminalUser = jpaBaseDao.getEntityById(TerminalUser.class, terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.CASHIERUSERID_NOT_EXIST);
        }

        return organizationDao.getOrganizationByNoIds(terminalUser.getOrganizationId(), provinceId);
    }

    @Override
    public List<Organization> getOrganizationByProvinceId(Long provinceId) {
        checkNotNull(provinceId);
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("auditStatus", AuditStatusEnum.HAS_AUDIT);
        conditions.put("provinceId", provinceId);
        return organizationDao.getEntitiesByFieldList(Organization.class, conditions);
    }

    @Override
    public List<Organization> getOrganizationByArea(Long id) {

        return organizationDao.getOrganizationByArea(id);
    }

    @Override
    public DomainPage getOrganizationInfoByDisCode(Map<String, Object> merchantALowerLevel, Map<String, Object> fuzzy, Long id, long pageIndex, long pageSize) {
        DomainPage domainPage = accountDao.getChannelMerchants(Organization.class, merchantALowerLevel, fuzzy, id, pageIndex, pageSize);
        if (domainPage.getDomains().size() > 0 && domainPage != null) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domainPage.getDomains().get(i);
                BigInteger bigInteger = new BigInteger(((Organization) objects[0]).getProvinceId().toString());
                long provinceId = bigInteger.longValue();
                Province province = jpaBaseDao.getEntityById(Province.class, provinceId);
                objects[1] = province;
                domainPage.getDomains().set(i, objects);
            }
        }
        return domainPage;
    }


    /**
     * 获取管理员管理的省并且封装成Map,以便查询Organization使用
     *
     * @param company
     * @return
     */
    private Map<String, List<Object>> getCompanyManagerProvince(Company company) {
        Map<String, List<Object>> collectionConditions = new HashMap<String, List<Object>>();
        List<Object> provinceIds = new ArrayList<Object>();
        String[] provinceArray = company.getArea().split(",");
        if (provinceArray.length > 0) {
            for (String pid : provinceArray) {
                provinceIds.add("'" + pid + "'");
            }
        }
        collectionConditions.put("provinceId", provinceIds);

        return collectionConditions;
    }

    /**
     * 根据商户注册时它对应的管理账号(即注册时的邮箱地址),得到有效账户
     *
     * @param collectionConditions
     * @param organizationManagerNames
     * @return
     */
    private List<Object[]> checkOrganizationByOrganizationManagerName(Map<String, List<Object>> collectionConditions, String organizationManagerNames) {
        List<Object[]> returnList = new ArrayList<Object[]>();

        String[] OManagerNameArray = organizationManagerNames.split(",");
        if (OManagerNameArray.length > 0) {
            for (String OManagerName : OManagerNameArray) {
                Object[] checkObj = new Object[3];

                Map<String, Object> conditions = new HashMap<String, Object>();
                conditions.put("accountname", OManagerName);
                conditions.put("positionRole", PositionRoleEnum.POSITION_ROLE_ADMIN);
                List<Object> provinceIds = collectionConditions.get("province.id");
                Map<String, List<Object>> cc = new HashMap<String, List<Object>>();
                cc.put("organization.provinceId", provinceIds);

                List<Account> accounts = presentPackDao.getEntitiesPagesByFieldList(Account.class, conditions, null, cc, null, null, 1, 10).getDomains();
                if (accounts != null && accounts.size() == 1) {
                    checkObj[0] = true;
                    checkObj[1] = accounts.get(0).getOrganizationId();
                    checkObj[2] = OManagerName;
                } else {
                    checkObj[0] = false;
                    checkObj[1] = null;
                    checkObj[2] = OManagerName;
                }
                returnList.add(checkObj);
            }
        }
        return returnList;
    }

    @Override
    public List<Organization> getOrganizationByProvince(Long provinceId) {
        checkNotNull(provinceId);
        return jpaBaseDao.getEntitiesByField(Organization.class, "provinceId", provinceId);
    }

    @Override
    public DomainPage selectIsNotParticularOrganization(Map<String, Object> fieldMap, long pageIndex, long pageSize) {
        /**1.获取数据库中的特约商户*/
        List<PointPoolOrganization> poolOrganizations = pointPoolOrganizationService.selectAllPointPoolOrganization();
        List<Long> idList = new ArrayList<Long>();
        if (poolOrganizations.size() > 0) {
            for (PointPoolOrganization pool : poolOrganizations) {
                idList.add(pool.getOrganizationId());
            }
        } else {
            idList.add(0L);
        }
        /**2.拼查询条件*/
        List<Expression> expressions = new ArrayList<Expression>();
        Expression exp = new Expression("auditStatus", Operation.Equal, AuditStatusEnum.HAS_AUDIT);
        Expression exp1 = new Expression("accountRole", Operation.Equal, AccountRoleEnum.ROLE_SUPPLIER);
        Expression exp2 = new Expression("id", Operation.NotIN, idList);
        expressions.add(exp);
        expressions.add(exp1);
        expressions.add(exp2);
        if (fieldMap.size() > 0) {
            if (fieldMap.get("organizationName") != null) {
                Expression exp3 = new Expression("organizationName", Operation.AllLike, "%" + fieldMap.get("organizationName") + "%");
                expressions.add(exp3);
            }
            if (fieldMap.get("provinceId") != null) {
                Expression exp4 = new Expression("provinceId", Operation.Equal, fieldMap.get("provinceId"));
                expressions.add(exp4);
            }
        }
        /**3.执行查询*/
        return jpaBaseDao.getEntitiesPagesByExpressionList(Organization.class, expressions, pageIndex, pageSize);
    }

    @Override
    public DomainPage selectWithPointPool(Long pointPoolId, long pageIndex, long pageSize) {
        return organizationDao.selectWithPointPool(pointPoolId, pageIndex, pageSize);
    }

    @Override
    public List<Organization> getOrganizationLists(Map<String, Object> fieldMap) {
        return jpaBaseDao.getEntitiesByFieldList(Organization.class, fieldMap);
    }

    @Override
    public List<Long> getOrganizationIDByAccountRole(AccountRoleEnum accountRole) {
        checkNotNull(accountRole);
        return organizationDao.getOrganizationIDByAccountRole(accountRole);
    }

    @Override
    public Long getAllOrganizationAmount() {
        return jpaBaseDao.getEntityTotalCount(Organization.class);
    }

    /**
     * 修改商户和积分池的关系
     * 如果该商户与积分池关系存在则放弃建立新关系
     *
     * @param pointPoolIdsStr PointPool ID的字符串，字符串以 “:” 的形势拼接
     *                        如：id1:id2:id3....
     * @param organizationId
     */
    private void createOrganizationPointPoolConnection(String pointPoolIdsStr, Long organizationId) {
        Preconditions.checkNotNull(pointPoolIdsStr);
        Preconditions.checkNotNull(organizationId);

        List<String> pointPoolIds = Splitter.on(SystemConst.REDIS_KEY_SEPARATOR_SYMBOL)
                .omitEmptyStrings()
                .trimResults()
                .splitToList(pointPoolIdsStr);

        // 删除旧的关系
        List<PointPoolOrganization> pointPoolOrganizations = pointPoolOrganizationService.selectByOrganizationId(organizationId);
        for (PointPoolOrganization poolOrganization : pointPoolOrganizations)
            pointPoolOrganizationService.deleteById(poolOrganization.getId());

        // 添加新的关系
        for (String pointPoolId : pointPoolIds) {
            Long poolId = Long.parseLong(pointPoolId);
            PointPoolOrganization poolOrganization = new PointPoolOrganization();
            poolOrganization.setOrganizationId(organizationId);
            poolOrganization.setPointPoolId(poolId);
            poolOrganization.setUpdateTime(DateTime.now().toDate());

            pointPoolOrganizationService.insert(poolOrganization);
        }

    }
}