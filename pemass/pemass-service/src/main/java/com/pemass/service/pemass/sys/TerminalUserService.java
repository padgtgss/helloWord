package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.redis.Token;

import java.util.List;
import java.util.Map;

/**
 * @author luoc
 * @Description: ${todo}
 * @date 2014/10/20
 */
public interface TerminalUserService {
    /**
     * 保存终端用户
     *
     * @param user
     */
    void saveTerminalUser(TerminalUser user);

    /**
     * 获取终端用户
     *
     * @param map
     * @param fuzzyMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getTerminalUserList(Map map, Map fuzzyMap, Long pageIndex, Long pageSize);

    /**
     * 获取满足条件的终端用户
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage<Object[]> getPagesByConditions(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 编辑终端用户信息（更新）
     *
     * @param target
     * @return
     */
    TerminalUser updateTerminalUser(TerminalUser target);

    /**
     * 根据id获取终端用户信息
     *
     * @param id
     * @return
     */
    TerminalUser getTerminalUser(Long id);

    /**
     * 根据id获取终端用户信息
     *
     * @param terminalUserId
     * @return
     */
    TerminalUser getTerminalById(Long terminalUserId);

    /**
     * 通过UUID获取终端用户
     *
     * @param uuid
     * @return
     */
    TerminalUser retrieveTerminalUserByUUID(String uuid);

    /**
     * 获取省下面的商户
     *
     * @param provinceId
     * @return
     */
    List<Organization> getOrganizationByProvince(Long provinceId);

    /**
     * B端用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param deviceId 设备唯一标示
     * @return
     */
    Token terminalLogin(String userName, String password, Long deviceId);

    /**
     * 根据用户uuid，查询用户信息
     *
     * @param uuid UUId
     * @return
     */
    TerminalUser retrieveUserByUUID(String uuid);

    /**
     * 修改密码
     *
     * @param id               B端用户id
     * @param originalPassword 旧密码
     * @param newPassword      新密码
     * @return
     */
    Boolean updatePassword(Long id, String originalPassword, String newPassword);

    /**
     * 修改用户信息
     *
     * @param terminalUser 用户对象
     * @return
     */
    Boolean updateEntiry(TerminalUser terminalUser);

    /**
     * 查询是否存在该用户
     *
     * @param username
     * @return
     */
    boolean hasTerminalUser(String username);

    /**
     * 根据条件获取所有的收银员id
     *
     * @param organizationId 机构id
     * @param isDistribution 是否分销（Y-分销，N-直销）
     * @return
     */
    List getAllCashierIdByField(Long organizationId, String isDistribution);

    /**
     * 重置终端账户登录密码
     *
     * @param terminalUser
     * @return
     */
    String updateTerminalUserPassword(TerminalUser terminalUser);
}
