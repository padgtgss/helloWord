/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.Token;

/**
 * @Description: 认证、权限等
 * @Author: estn.zuo
 * @CreateTime: 2014-11-16 16:00
 */
public interface AuthService {

    /**
     * C端用户注册
     * <p/>
     * 只能通过手机号注册
     *
     * @param user
     * @return
     */
    User register(User user);

    /**
     * C端用户登录
     *
     * @param username 用户名
     * @param password 密码   (解密后密码)
     * @param deviceId 设备ID
     * @return
     */
    Token login(String username, String password, Long deviceId,String longitude,String latitude);

    /**
     * 忘记密码
     *
     * @param username 用户名
     * @return
     */
    Boolean forgetPassord(String username,String password);

    /**
     * 更新密码
     *
     * @param uid              用户ID
     * @param originalPassword 原始密码
     * @param newPassword      新密码
     * @return
     */
    Boolean updatePassword(Long uid, String originalPassword, String newPassword);
}