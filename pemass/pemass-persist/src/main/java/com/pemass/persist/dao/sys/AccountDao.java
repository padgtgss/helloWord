package com.pemass.persist.dao.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.biz.NotificationOrganization;
import com.pemass.persist.domain.jpa.sys.Account;

import java.util.Map;

/**
 * Created by estn.zuo on 14-9-18.
 */
public interface AccountDao extends BaseDao {

    com.pemass.persist.domain.jpa.sys.Account getAccountByUsername(String username);

    Account getEntityAccount(Account account, String accountname, String accountname1);

    /**
     * 用户权限查询下级账户信息
     *
     * @param clazz
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage getLevelAccountList(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 查询下级商户个数
     *
     * @param clazz
     * @param fieldNameValueMap
     * @return
     */
    Long getAccountManageCount(Class clazz, Map<String, Object> fieldNameValueMap);

    NotificationOrganization getNotificationOrganization(Long messageId, Long organizationId);

    DomainPage selectnotificationList(Long organization_id, String isRead, Long pageIndex, Long pageSize);

    Account hasBeing(Class clazz, String name);

    /**
     * 渠道商查询自己的商户
     *
     * @param clazz
     * @param fieldNameValueMap      精确匹配条件
     * @param fuzzyFieldNameValueMap 模糊匹配条件
     * @param organizationId         渠道商id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getChannelMerchants(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long organizationId, long pageIndex, long pageSize);

    /**
     * 获取渠道商的下级分销商
     *
     * @param conditions
     * @return
     */
    DomainPage getChannelLowerOrganization(Map<String, Object> conditions,DomainPage domainPage);
}
