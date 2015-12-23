package com.pemass.service.pemass.sys.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.dao.sys.AccountDao;
import com.pemass.persist.domain.jpa.biz.NotificationOrganization;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.AccountStatusEnum;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PositionRoleEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.biz.NotificationService;
import com.pemass.service.pemass.sys.AccountService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: AccountServiceImpl
 * Author: estn.zuo
 * CreateTime: 2014-09-19 10:08
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private NotificationService notificationService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private SequenceService sequenceService;


    @Override
    public Account selectByAccountname(String Accountname) {

        return jpaBaseDao.getEntityByField(Account.class, "accountname", Accountname);

    }

    @Transactional
    @Override
    public Account register(Account account) {
        /**1.生成盐值*/
        String salt = UUIDUtil.randomChar(8);

        /**2.生成密码*/
        String encryptPassword = MD5Util.encrypt(account.getPassword(), salt);

        /**3.保存用户基本数据*/
        account.setPassword(encryptPassword);
        account.setSalt(salt);
        account.setRegistrationTime(new Date());
        account.setAccountStatus(AccountStatusEnum.ACTIVATE); //状态(默认激活)
        account.setPositionRole(PositionRoleEnum.POSITION_ROLE_ADMIN);//  职务角色
        account.setAccountStatus(AccountStatusEnum.ACTIVATE); //账号状态
        account.setAuthority("ROLE_SUPPLIER");//角色(默认为供应商)

        /**4,添加账户表*/
        accountDao.persist(account);

        return account;
    }

    @Transactional
    @Override
    public void registerChannelOrganization(Account account, Organization organization) {
        /*-- 持久化organization --*/
        addChannelOrganization(organization);

        /*-- 注册账号 --*/
        addChannelAccount(account, organization);
    }

    private void addChannelOrganization(Organization organization) {
        /**生成商户编号并赋值*/
        String identifier = sequenceService.obtainSequence(SequenceEnum.ORGANIZATION);
        organization.setOrganizationIdentifier(identifier);
        organization.setAuditStatus(AuditStatusEnum.HAS_AUDIT);
        organization.setAccountRole(AccountRoleEnum.ROLE_CHANNEL);
        organization.setOneAuditStatus(AuditStatusEnum.NONE_AUDIT);
        organization.setIsOneMerchant(0);
//        organization.setDistributionCode(UUIDUtil.randomNumber(6));
        jpaBaseDao.persist(organization);
    }

    private void addChannelAccount(Account account, Organization organization) {
        account.setAuthority("ROLE_CHANNEL");

        /**1.生成盐值*/
        String salt = UUIDUtil.randomChar(8);

        /**2.生成密码*/
        String encryptPassword = MD5Util.encrypt(account.getPassword(), salt);

        /**3.保存用户基本数据*/
        account.setPassword(encryptPassword);
        account.setSalt(salt);
        account.setRegistrationTime(new Date());
        account.setAccountStatus(AccountStatusEnum.ACTIVATE); //状态(默认激活)

        account.setPositionRole(PositionRoleEnum.POSITION_ROLE_ADMIN);//  职务角色
        account.setRegistrationTime(new Date());
        account.setAccountStatus(AccountStatusEnum.ACTIVATE); //账号状态
        account.setOrganizationId(organization.getId());

        /**4,添加账户表*/
        accountDao.persist(account);
    }

    public boolean hasBeing(String username) {
        Account storeAccount = accountDao.hasBeing(Account.class, username);
        return storeAccount != null;
    }

    public void updatepwd(Account account, String newpwd) {//修改个人登录密码
        account.setPassword(newpwd);
        account.setUpdateTime(new Date());
        accountDao.merge(account);

    }

    @Override
    public Account selectByAccountId(Long accountId) {

        return jpaBaseDao.getEntityById(Account.class, accountId);
    }


    @Override
    public void updatetelephone(Account account, String newtelephone) {
        account.setTelephone(newtelephone);
        accountDao.merge(account);
    }

    @Override
    public void updatepaypwd(Account account, String newpwd2) {
        account.setUpdateTime(new Date());
        accountDao.merge(account);
    }

    @Override
    public DomainPage selectnotificationList(Long organization_id, String isRead, Long pageIndex, Long pageSize) {
        DomainPage domainPage = accountDao.selectnotificationList(organization_id, isRead, pageIndex, pageSize);
        List<Object[]> list = Lists.newArrayList();
        if (domainPage.getDomains() != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domainPage.getDomains().get(i);
                objects[1] = notificationService.getNotificationById(((NotificationOrganization) objects[0]).getNotificationId());
                list.add(objects);
            }
        }
        domainPage.setDomains(list);
        return domainPage;

    }

    @Override
    public Account updateConfigInfo(Account account, Organization organization) {
        Account account1 = null;
        if (account != null) {
            account1 = accountDao.merge(account);
        }
        if (organization != null) {
            accountDao.merge(organization);
        }
        return account1;
    }

    @Override
    public Account getEntityAccount(Account account, String accountname, String fieldValue) {
        return accountDao.getEntityAccount(account, accountname, fieldValue);
    }

    @Override
    public void updateAvatar(Account account) {
        accountDao.merge(account);
    }

    @Transactional
    @Override
    public NotificationOrganization updateByMessageId(Long MessageId, Long organizationId) {//用户阅读后更新阅读状态
        NotificationOrganization notificationOrganization = accountDao.getNotificationOrganization(MessageId, organizationId);
        notificationOrganization.setIsRead(true);

        return accountDao.merge(notificationOrganization);
    }

    @Transactional
    @Override
    public void deleteMessage(Long MessageId, Long organizationId) {
        NotificationOrganization notificationOrganization = accountDao.getNotificationOrganization(MessageId, organizationId);
        notificationOrganization.setAvailable(AvailableEnum.UNAVAILABLE);

        accountDao.merge(notificationOrganization);
    }

    @Override
    public int selectnotificationNum(Organization organization) {
        Map map = new HashMap();
        map.put("organizationId", organization.getId());
        map.put("isRead", false);

        List<NotificationOrganization> notificationOrganizationList = accountDao.getEntitiesByFieldList(NotificationOrganization.class, map);
        return notificationOrganizationList.size();
    }

    @Override
    public Account getAccountByConditions(Long organizationId, PositionRoleEnum positionRoleEnum) {
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("organizationId", organizationId);
        conditions.put("positionRole", positionRoleEnum);
        List<Account> accounts = jpaBaseDao.getEntitiesByFieldList(Account.class, conditions);
        return (accounts == null || accounts.size() != 1) ? null : accounts.get(0);
    }

    @Override
    public Account updateAccount(Account account) {
        Account targetAccount = jpaBaseDao.getEntityById(Account.class, account.getId());
        targetAccount = (Account) MergeUtil.merge(account, targetAccount);
        return jpaBaseDao.merge(targetAccount);
    }

    /**
     * 用户权限添加下级账户信息
     *
     * @param account
     * @return
     */
    @Transactional
    @Override
    public Account addAccount(Account account) {
        /**2.生成盐值*/
        String salt = UUIDUtil.randomChar(8);

        /**3.生成密码*/
        String encryptPassword = MD5Util.encrypt(account.getPassword(), salt);

        /**4.保存用户基本数据*/
        account.setPassword(encryptPassword);
        account.setSalt(salt);
        account.setRegistrationTime(new Date());
        account.setAccountStatus(AccountStatusEnum.ACTIVATE); //状态(默认激活)
        Organization organization1 = organizationService.getOrganizationById(account.getOrganizationId());
        account.setAuthority(organization1.getAccountRole().toString());//权限

//        account.setPositionRole(PositionRoleEnum.POSITION_ROLE_ADMIN);//  职务角色
        account.setRegistrationTime(new Date());
        account.setAccountStatus(AccountStatusEnum.ACTIVATE); //账号状态

        accountDao.persist(account);
        return account;
    }

    /**
     * 用户权限获取下级账户列表
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public DomainPage getLevelAccountList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        return accountDao.getLevelAccountList(Account.class, fieldNameValueMap, fuzzyFieldNameValueMap, pageIndex, pageSize);
    }

    /**
     * 修改登录时间
     *
     * @param username
     */
    @Override
    public void updateLoginTime(String username) {
        Account account = jpaBaseDao.getEntityByField(Account.class, "accountname", username);
        account.setLastLoginTime(new Date());
        jpaBaseDao.merge(account);
    }

    /**
     * 重置下级账户登录密码
     *
     * @param account
     * @return
     */
    @Override
    public String updatePassword(Account account) {
        String newPassword = UUIDUtil.getStringRandom(6);
        String password = MD5Util.encrypt(newPassword, account.getSalt());
        account.setPassword(password);
        account.setUpdateTime(new Date());
        accountDao.merge(account);
        return newPassword;
    }

    @Override
    public DomainPage getChannelOrganization(Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        DomainPage domainPage1 = presentPackDao.getEntitiesPagesByList(fuzzyConditions, domainPage);
        List<Object[]> list = Lists.newArrayList();
        if (domainPage1 != null && domainPage1.getDomains().size() > 0) {
            for (int i = 0; i < domainPage1.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domainPage1.getDomains().get(i);
                if (((Account) objects[0]).getOrganizationId() != 0) {
                    objects[1] = organizationService.getOrganizationById(((Account) objects[0]).getOrganizationId());
                }
                list.add(objects);
            }
        }
        domainPage1.setDomains(list);
        return domainPage1;
    }

    @Override
    public boolean getAccountByTelephone(String telephone) {
        Account account = jpaBaseDao.getEntityByField(Account.class, "telephone", telephone);
        return account == null;
    }

    @Override
    public boolean getAccountByAccountname(String accountname) {
        boolean anwser = false;
        Account account = jpaBaseDao.getEntityByField(Account.class, "accountname", accountname);
        if (account != null)  anwser = true;
        return anwser;
    }

    @Override
    public Account getAccountById(Long id) {
        return jpaBaseDao.getEntityById(Account.class,id);
    }
}
