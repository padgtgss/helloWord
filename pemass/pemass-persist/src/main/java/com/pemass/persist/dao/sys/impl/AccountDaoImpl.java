package com.pemass.persist.dao.sys.impl;

import com.google.common.collect.Lists;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.dao.sys.AccountDao;
import com.pemass.persist.domain.jpa.biz.NotificationOrganization;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.Organization;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: 账户DAO
 * Author: estn.zuo
 * CreateTime: 2014-09-18 18:17
 */
@Repository
public class AccountDaoImpl extends JPABaseDaoImpl implements AccountDao {


    @Override
    public com.pemass.persist.domain.jpa.sys.Account getAccountByUsername(String username) {
        return null;
    }

    @Override
    public Account getEntityAccount(Account account, String accountname, String fieldValue) {

        StringBuffer sb = new StringBuffer();
        sb.append("from Account a ");
        sb.append(" left join fetch a.province p ");
        sb.append(" left join fetch a.city c ");
        sb.append(" left join fetch a.district d ");
        sb.append(" where  a.").append(accountname).append("=?1 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, fieldValue);
        List<Account> ret = query.getResultList();
        if (ret == null || ret.size() < 1) {
            return null;
        }
        //TODO how to deal with multiple record.
        return ret.get(0);
    }

    /**
     * 用户权限查询下级账户信息
     *
     * @param clazz
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseDomain> DomainPage getLevelAccountList(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = ((pageIndex < 1) ? 1 : pageIndex);
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果
        String sql = "select c from " + clazz.getName() + " c where ";
        sql = sql + " c.available = 1";
        sql = sql + " and c.positionRole <> 'POSITION_ROLE_ADMIN'";
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }


        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<T> resultList = query.getResultList();

        //查询总记录
        sql = "select count(c) from " + clazz.getName() + " c where ";
        sql = sql + " c.available = 1";
        sql = sql + " and c.positionRole <> 'POSITION_ROLE_ADMIN'";
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }


        sql = sql + " order by c.updateTime desc";
        query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }

        Long totalCount = (Long) query.getResultList().get(0);

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public Long getAccountManageCount(Class clazz, Map<String, Object> fieldNameValueMap) {
        String sql = "select c from " + clazz.getName() + " c where ";
        sql = sql + " c.available = 1";
        sql = sql + " and c.positionRole <> 'POSITION_ROLE_ADMIN'";
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }


        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }

        List<Account> resultList = query.getResultList();


        return Long.valueOf(resultList.size());
    }

    @Override
    public NotificationOrganization getNotificationOrganization(Long messageId, Long organizationId) {
        String sql = "select * from biz_notification_organization t where 1=1 and notification_id = " + messageId + " and organization_id = " + organizationId;
        Query query = em.createNativeQuery(sql, NotificationOrganization.class);
        NotificationOrganization notificationOrganization = (NotificationOrganization) query.getResultList().get(0);
        return notificationOrganization;
    }

    @Override
    public DomainPage selectnotificationList(Long organization_id, String isRead, Long pageIndex, Long pageSize) {
        pageIndex = ((pageIndex < 1) ? 1 : pageIndex);
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c.* FROM biz_notification t , biz_notification_organization c " +
                "where t.id = c.notification_id " +
                "AND c.organization_id =  " + organization_id + " ";


        if (StringUtils.isNotBlank(isRead)) {
            sql += "AND c.is_read =" + isRead + " ";
        }
        sql += "AND c.available = 1 " +
                "AND t.available = 1 " +
                "ORDER BY t.issue_time DESC ";
        Query query = em.createNativeQuery(sql, NotificationOrganization.class);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults(pageSize.intValue());

        List<T> resultList = query.getResultList();

        Query count = em.createNativeQuery(sql, NotificationOrganization.class);
        Long totalCount = Long.valueOf(count.getResultList().size() + "");
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public Account hasBeing(Class clazz, String name) {
        String sql = "select c from " + clazz.getName() + " c where c.accountname =  '" + name + "'";
        Query query = em.createQuery(sql);
        List<Account> accounts = query.getResultList();
        if (accounts.size() < 1 || accounts == null) {
            return null;
        } else {
            return accounts.get(0);
        }
    }

    @Override
    public DomainPage getChannelMerchants(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long organizationId, long pageIndex, long pageSize) {
        pageIndex = ((pageIndex < 1) ? 1 : pageIndex);
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果
        String sql = "select c from " + clazz.getName() + " c where ";
        sql = sql + " c.available = 1";
        sql = sql + " and c.id <> " + organizationId;
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }


        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<T> resultList = query.getResultList();

        //查询总记录
        sql = "select count(c) from " + clazz.getName() + " c where ";
        sql = sql + " c.available = 1";
        sql = sql + " and c.id <> " + organizationId;
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }


        sql = sql + " order by c.updateTime desc";
        query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }

        Long totalCount = (Long) query.getResultList().get(0);

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public DomainPage getChannelLowerOrganization(Map<String, Object> conditions, DomainPage domainPage) {
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();

        String distributionCode = (String) conditions.get("distributionCode");
        String organizationName = (String) conditions.get("organizationName");

        StringBuilder sql = new StringBuilder("SELECT * ");
        sql.append(" FROM sys_organization");
        sql.append(" WHERE distribution_code = '" + distributionCode + "'");
        sql.append(" AND account_role <> 5");
        if (StringUtils.isNotBlank(organizationName)) {
            sql.append(" AND organization_name like '%" + organizationName + "%'");
        }
        sql.append(" ORDER BY create_time DESC");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize + ";");

        Query query = em.createNativeQuery(sql.toString(), Organization.class);
        List<Organization> resultList = query.getResultList();

        List<Account> resultAccounts = Lists.newArrayList();
        // 获取商户的管理员信息
        getChannelOrganizationAccount(resultList, resultAccounts);

        sql = new StringBuilder("SELECT COUNT(id)");
        sql.append(" FROM sys_organization");
        sql.append(" WHERE distribution_code = '" + distributionCode + "'");
        sql.append(" AND account_role <> 5");
        if (StringUtils.isNotBlank(organizationName)) {
            sql.append(" AND organization_name like '%" + organizationName + "%'");
        }
        long totalCount = 0L;
        query = em.createNativeQuery(sql.toString());
        if (query.getResultList().size() == 1) {
            totalCount = Long.parseLong(query.getResultList().get(0).toString());
        }

        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(resultAccounts);

        return returnDomainPage;
    }

    private void getChannelOrganizationAccount(List<Organization> resultList, List<Account> resultAccounts) {
        StringBuilder sql;
        Query query;
        for (Organization organization : resultList) {
            sql = new StringBuilder("SELECT * FROM sys_account");
            sql.append(" WHERE position_role = 'POSITION_ROLE_ADMIN'");
            sql.append(" AND organization_id = " + organization.getId());
            query = em.createNativeQuery(sql.toString(), Account.class);
            List<Account> accounts = query.getResultList();
            if (accounts.size() == 1) {
                Account account = accounts.get(0);
                account.setOrganizationId(organization.getId());
                resultAccounts.add(account);
            }
        }
    }
}

