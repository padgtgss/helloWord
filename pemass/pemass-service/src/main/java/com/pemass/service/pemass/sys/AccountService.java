package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.NotificationOrganization;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.PositionRoleEnum;

import java.util.Map;

/**
 * Created by estn.zuo on 14-9-19.
 */
public interface AccountService {

    /**
     * 根据用户名查找账号
     *
     * @param accountname
     * @return
     */
    Account selectByAccountname(String accountname);

    /**
     * 注册账号
     * account 用户对象
     *
     * @return
     * @throws java.lang.IllegalArgumentException 当用户名已经存在时，抛出异常
     */
    Account register(Account account);


    /**
     * 审核后台 注册渠道商
     *
     * @param account
     * @param organization
     */
    void registerChannelOrganization(Account account, Organization organization);

    /**
     * 修改密码
     * String newpwd
     *
     * @return
     */
    void updatepwd(Account account, String updatepwd);

    /**
     * 根据用户id查询
     * Account accountId
     *
     * @return
     */
    Account selectByAccountId(Long accountId);


    /**
     * 判断用户名是否重复
     *
     * @param username
     * @return
     */
    boolean hasBeing(String username);

    /**
     * 设置绑定手机
     *
     * @param account,newtelephone
     * @return
     */
    void updatetelephone(Account account, String newtelephone);

    /**
     * 设置支付密码
     *
     * @param account,newpwd2
     * @return
     */
    void updatepaypwd(Account account, String newpwd2);

    /**
     * 根据用户ID获取站内消息
     *
     * @param id
     * @return
     */
    DomainPage selectnotificationList(Long id, String isRead, Long pageIndex, Long pageSize);

    /**
     * 更新账户信息
     * <p/>
     * account和 organization谁不为空就更新谁
     *
     * @param account
     * @param organization
     */
    Account updateConfigInfo(Account account, Organization organization);

    Account getEntityAccount(Account account, String accountname, String fieldValue);

    void updateAvatar(Account account);

    NotificationOrganization updateByMessageId(Long MessageId, Long organizationId);

    void deleteMessage(Long MessageId, Long organizationId);

    int selectnotificationNum(Organization organization);

    /**
     * 根据商户获取该商户的管理员（联系人）（审核后台）
     *
     * @param organizationId
     * @param positionRoleEnum
     * @return
     */
    Account getAccountByConditions(Long organizationId, PositionRoleEnum positionRoleEnum);

    /**
     * 跟新账户（审核后台）
     *
     * @return
     */
    Account updateAccount(Account account);

    /**
     * 用户权限添加下级账户
     *
     * @param account
     * @return
     */
    Account addAccount(Account account);

    /**
     * 用户权限获取下级账户信息
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getLevelAccountList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 修改登录时间
     *
     * @param username
     */
    void updateLoginTime(String username);

    /**
     * 用户重置密码
     *
     * @param account
     * @return
     */
    String updatePassword(Account account);

    /**
     * 获取满足条件的渠道商
     *
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage getChannelOrganization(Map<String, Object> fuzzyConditions, DomainPage domainPage);

    boolean getAccountByTelephone(String telephone);

    /**
     * 根据accountname获取用户
     * @param accountname
     * @return
     */
    boolean getAccountByAccountname(String accountname);

    /**
     * 根据ID获取用户
     * @param id
     * @return
     */
    Account getAccountById(Long id);

}
