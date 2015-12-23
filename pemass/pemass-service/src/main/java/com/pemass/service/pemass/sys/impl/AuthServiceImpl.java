/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.exception.DefaultError;
import com.pemass.common.core.util.BarCodeUtil;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.QRCodeUtil;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.enumeration.GenderEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.sys.UserDao;
import com.pemass.persist.domain.jpa.bas.Device;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.Token;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.sys.AuthService;
import com.pemass.service.pemass.sys.TokenService;
import com.pemass.service.pemass.sys.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: AuthServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-11-16 16:07
 */
@Service
public class AuthServiceImpl implements AuthService {

    private Log logger = LogFactory.getLog(AuthServiceImpl.class);

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    @Resource
    private TokenService tokenService;

    @Transactional
    @Override
    public User register(User user) {

        User resultUser = userDao.getUserByUsername(user.getUsername());
        if (resultUser != null && resultUser.getAvailable().equals(AvailableEnum.AVAILABLE)) {
            throw new BaseBusinessException(SysError.USERNAME_HAS_EXISTENCE);
        }

        /** 1.生成盐值*/
        String salt = UUIDUtil.randomChar(8);
        /** 2.生成密码*/
        String encryptPassword = MD5Util.encrypt(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(encryptPassword);
        user.setAuthority(PemassConst.ROLE_USER);
        user.setRegisterTime(new Date());
        user.setQrCode(QRCodeUtil.encodeQRCode(user.getUsername()));
        user.setBarCode(BarCodeUtil.encode(user.getUsername()));
        user.setIsCredited(0);
        if (user.getGender() == null) {
            user.setGender(GenderEnum.MALE);
        }

        if (resultUser != null && resultUser.getAvailable().equals(AvailableEnum.FROZEN)) {
            MergeUtil.merge(user,resultUser);
            resultUser.setAvailable(AvailableEnum.AVAILABLE);
            jpaBaseDao.merge(resultUser);
            return resultUser;
        }

        /** 4.保存数据 */
        if (resultUser == null){
            jpaBaseDao.persist(user);
            return user;
        }

        return null;
    }

    @Transactional
    @Override
    public Token login(String username, String password, Long deviceId, String longitude, String latitude) {

        User user = jpaBaseDao.getEntityByField(User.class, "username", username);
        /**用户名是否正确*/
        if (user == null) {
            throw new BaseBusinessException(SysError.USERNAME_NOT_EXIST);
        }

        /**判断密码是否正确*/
        if (!MD5Util.verify(password, user.getSalt(), user.getPassword())) {
            throw new BaseBusinessException(SysError.PASSWORD_ERROR);
        }

        /**设备是否存在*/
        Device device = jpaBaseDao.getEntityById(Device.class, deviceId);
        if (device == null) {
            throw new BaseBusinessException(DefaultError.DEVICE_NOT_EXIST);
        }

        /**插入token表*/
        Token newToken = new Token();
        newToken.setDeviceId(device.getId());
        newToken.setTargetUUID(user.getUuid());
        newToken.setAuthority(user.getAuthority());
        newToken.setTargetId(user.getId());
        tokenService.insert(newToken);

        /**更新用户最后一次经纬度*/
        user.setLongitude(longitude);
        user.setLatitude(latitude);
        jpaBaseDao.merge(user);
        return newToken;
    }

    @Transactional
    @Override
    public Boolean forgetPassord(String username, String password) {
        User user = userService.getByUsername(username);
        user.setPassword(MD5Util.encrypt(password, user.getSalt()));
        jpaBaseDao.merge(user);
        tokenService.delToken(user.getUuid());
        return true;
    }


    @Override
    public Boolean updatePassword(Long uid, String originalPassword, String newPassword) {

        User user = jpaBaseDao.getEntityById(User.class, uid);
        /**判断原密码是否正确*/
        if (!MD5Util.verify(originalPassword, user.getSalt(), user.getPassword())) {
            throw new BaseBusinessException(SysError.PASSWORD_ERROR);
        }

        String newpassword = MD5Util.encrypt(newPassword, user.getSalt());
        user.setPassword(newpassword);
        jpaBaseDao.merge(user);

        tokenService.delToken(user.getUuid());

        return true;
    }
}
