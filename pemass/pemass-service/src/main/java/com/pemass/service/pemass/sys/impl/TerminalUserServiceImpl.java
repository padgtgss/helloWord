package com.pemass.service.pemass.sys.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.exception.DefaultError;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.dao.sys.TerminalUserDao;
import com.pemass.persist.domain.jpa.bas.Device;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.redis.Token;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.AccountStatusEnum;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.sys.TerminalUserService;
import com.pemass.service.pemass.sys.TokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: TerminalUserServiceImpl
 * @Author: luoc
 * @CreateTime: 2014-10-20 16:46
 */
@Service
public class TerminalUserServiceImpl implements TerminalUserService {

    private Log logger = LogFactory.getLog(TerminalUserServiceImpl.class);

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private TerminalUserDao terminalUserDao;

    @Resource
    private TokenService tokenService;

    @Resource
    private SiteService siteService;

    /**
     * 保存终端用户
     *
     * @param user
     */
    @Override
    public void saveTerminalUser(TerminalUser user) {
        /**2.生成盐值*/
        String salt = UUIDUtil.randomChar(8);

        /**3.生成密码*/
        String encryptPassword = MD5Util.encrypt(user.getPassword(), salt);
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        user.setCreateTime(new Date());
        jpaBaseDao.persist(user);
    }

    /**
     * 获取终端用户集合
     *
     * @param map
     * @param fuzzyMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public DomainPage getTerminalUserList(Map map, Map fuzzyMap, Long pageIndex, Long pageSize) {
        DomainPage domainPage = presentPackDao.getEntitiesPagesByFieldList(TerminalUser.class, map, fuzzyMap, pageIndex, pageSize);
        if (domainPage != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domainPage.getDomains().get(i);
                if (((TerminalUser) objects[0]).getSiteId() != null || ((TerminalUser) objects[0]).getSiteId() != 0) {
                    Site site = siteService.getSiteById(((TerminalUser) objects[0]).getSiteId());
                    objects[1] = site;
                }
                domainPage.getDomains().set(i, objects);
            }
        }
        return domainPage;

    }

    @Override
    public DomainPage<Object[]> getPagesByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        checkNotNull(conditions);
        checkNotNull(domainPage);
        return terminalUserDao.getPagesByConditions(conditions, domainPage);
    }


    /**
     * 编辑终端用户信息
     *
     * @param target
     * @return
     */
    @Override
    public TerminalUser updateTerminalUser(TerminalUser target) {
        TerminalUser originTarget = jpaBaseDao.getEntityById(TerminalUser.class, target.getId());
        MergeUtil.merge(target, originTarget);
        return jpaBaseDao.merge(originTarget);
    }

    /**
     * 根据id 获取终端用户信息
     *
     * @param id
     * @return
     */
    @Override
    public TerminalUser getTerminalUser(Long id) {
        return jpaBaseDao.getEntityById(TerminalUser.class, id);
    }

    @Override
    public TerminalUser getTerminalById(Long terminalUserId) {
        TerminalUser terminalUser = jpaBaseDao.getEntityById(TerminalUser.class, terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.CASHIERUSERID_NOT_EXIST);
        }
        return terminalUser;
    }

    @Override
    public TerminalUser retrieveTerminalUserByUUID(String uuid) {
        return jpaBaseDao.getEntityByField(TerminalUser.class, "uuid", uuid);
    }

    @Override
    public List<Organization> getOrganizationByProvince(Long provinceId) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("provinceId",provinceId);
        map.put("accountRole", AccountRoleEnum.ROLE_SUPPLIER);
        return jpaBaseDao.getEntitiesByFieldList(Organization.class, map);
    }

    /**
     * B端用户登录
     */
    @Override
    @Transactional
    public Token terminalLogin(String userName, String password, Long deviceId) {

        if (logger.isDebugEnabled()) {
            logger.debug("解密后：[password=" + password + "]");
        }

        TerminalUser terminalUser = jpaBaseDao.getEntityByField(TerminalUser.class, "terminalUsername", userName);

        /**用户名是否正确*/
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.USERNAME_NOT_EXIST);
        }

        /**用户是否被冻结*/
        if (terminalUser.getAccountStatus().equals(AccountStatusEnum.FREEZE)){
            throw new BaseBusinessException(SysError.USER_IS_FREEZE);
        }

        /**判断密码是否正确*/
        if (!MD5Util.verify(password, terminalUser.getSalt(), terminalUser.getPassword())) {
            throw new BaseBusinessException(SysError.PASSWORD_ERROR);
        }

        /**设备是否存在*/
        Device device = jpaBaseDao.getEntityById(Device.class, deviceId);
        if (device == null) {
            throw new BaseBusinessException(DefaultError.DEVICE_NOT_EXIST);
        }

        /**插入token表*/
        Token token = new Token();
        token.setDeviceId(device.getId());
        token.setTargetUUID(terminalUser.getUuid());
        token.setAuthority(terminalUser.getAuthority());
        token.setTargetId(terminalUser.getId());
        tokenService.insert(token);

        return token;
    }

    /**
     * 根据用户UUId查询
     *
     * @param uuid UUId
     * @return
     */
    @Override
    public TerminalUser retrieveUserByUUID(String uuid) {

        return jpaBaseDao.getEntityByField(TerminalUser.class, "uuid", uuid);
    }

    /**
     * 修改密码
     *
     * @param id               B端用户id
     * @param originalPassword 旧密码
     * @param newPassword      新密码
     * @return
     */
    @Override
    public Boolean updatePassword(Long id, String originalPassword, String newPassword) {
        TerminalUser terminalUser = jpaBaseDao.getEntityById(TerminalUser.class, id);

        /**判断原密码是否正确*/
        if (!MD5Util.verify(originalPassword, terminalUser.getSalt(), terminalUser.getPassword())) {
            throw new BaseBusinessException(SysError.PASSWORD_ERROR);
        }

        String newpassword = MD5Util.encrypt(newPassword, terminalUser.getSalt());
        terminalUser.setPassword(newpassword);
        jpaBaseDao.merge(terminalUser);
        tokenService.delToken(terminalUser.getUuid());
        return true;
    }

    /**
     * 修改用户信息
     *
     * @param terminalUser 用户对象
     * @return
     */
    @Override
    public Boolean updateEntiry(TerminalUser terminalUser) {
        TerminalUser newTerminalUser = jpaBaseDao.getEntityById(TerminalUser.class, terminalUser.getId());
        if (newTerminalUser == null) {
            throw new BaseBusinessException(SysError.USERNAME_NOT_EXIST);
        }
        MergeUtil.merge(terminalUser, newTerminalUser, new String[]{"terminalUsername", "password", "salt", "authority"});
        jpaBaseDao.merge(newTerminalUser);
        return true;
    }

    @Override
    public boolean hasTerminalUser(String username) {
        return terminalUserDao.hasBeing(TerminalUser.class, username) != null;
    }

    @Override
    public List getAllCashierIdByField(Long organizationId, String isDistribution) {
        return terminalUserDao.getAllCashierIdByField(organizationId, isDistribution);
    }

    /**
     * 重置终端账户登录密码
     *
     * @param terminalUser
     * @return
     */
    @Override
    public String updateTerminalUserPassword(TerminalUser terminalUser) {
        String newPassword = UUIDUtil.getStringRandom(6);
        String password = MD5Util.encrypt(newPassword, terminalUser.getSalt());
        terminalUser.setPassword(password);
        terminalUser.setUpdateTime(new Date());
        terminalUserDao.merge(terminalUser);
        tokenService.delToken(terminalUser.getUuid());
        return newPassword;
    }
}
