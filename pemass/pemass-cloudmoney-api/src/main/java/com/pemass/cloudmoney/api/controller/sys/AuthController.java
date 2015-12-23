package com.pemass.cloudmoney.api.controller.sys;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.App;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.domain.redis.Token;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.pojo.sys.TokenPojo;
import com.pemass.pojo.sys.UserPojo;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.ec.OrderService;
import com.pemass.service.pemass.sys.AppService;
import com.pemass.service.pemass.sys.AuthService;
import com.pemass.service.pemass.sys.ConfigService;
import com.pemass.service.pemass.sys.OrganizationService;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Description: C端用户登录授权
 * Author: estn.zuo
 * CreateTime: 2014-07-12 14:43
 */
@Controller
@RequestMapping("auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private AppService appService;

    @Resource
    private SmsMessageService smsMessageService;

    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    @Resource
    private ConfigService configService;

    @Resource
    private OrganizationService organizationService;

    /**
     * C端用户登录
     *
     * @param username  用户名
     * @param password  密码
     * @param deviceId  设备ID
     * @param longitude 精度
     * @param latitude  维度
     * @param appid     应用ID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String username, String password, Long deviceId, String longitude, String latitude, String appid) throws Exception {

        App app = appService.getAppById(appid);

        Token token = authService.login(username, AESUtil.decrypt(password, app.getKey()), deviceId, longitude, latitude);

        User user = userService.retrieveByUUID(token.getTargetUUID());

        /**封装返回数据*/
        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setToken(token.getToken());

        UserPojo userPojo = new UserPojo();
        MergeUtil.merge(user, userPojo);
        tokenPojo.setUser(userPojo);

        return tokenPojo;
    }


    /**
     * C端用户注册
     *
     * @param username     用户名(手机号码)
     * @param password     密码(AES加密后的密码)
     * @param validateCode 验证码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(String username, String password, String validateCode, String appid) throws Exception {
        /**验证验证码*/
        smsMessageService.validateCode(username, SmsTypeEnum.REG_VAL, validateCode);

        App app = appService.getAppById(appid);

        /**注册用户*/
        User user = new User();
        user.setUsername(username);
        user.setPassword(AESUtil.decrypt(password, app.getKey()));
        user = authService.register(user);

        /**赠送用户200E积分*/
        Config config = configService.getConfigByKey(ConfigEnum.JFT_ORGANIZATION_ID);
        Long id = Long.valueOf(config.getValue());
        Organization organization = organizationService.getOrganizationById(id);
        orderService.givingPoint(organization,user.getId(), PointTypeEnum.P,200);

        /**返回数据*/
        UserPojo userPojo = new UserPojo();
        MergeUtil.merge(user, userPojo);
        return userPojo;
    }


    /**
     * 找回密码
     *
     * @param validateCode 验证码
     * @param username     用户名
     * @param password     密码
     * @return
     */
    @RequestMapping(value = "forget", method = RequestMethod.POST)
    @ResponseBody
    public Object forgetPassword(String validateCode, String username, String password, String appid) throws Exception {
        App app = appService.getAppById(appid);
        /** 验证验证码 */
        smsMessageService.validateCode(username, SmsTypeEnum.FIND_PWD, validateCode);
        authService.forgetPassord(username, AESUtil.decrypt(password, app.getKey()));
        return ImmutableMap.of("result", true);
    }

    /**
     * 修改密码
     *
     * @param uid              用户ID
     * @param originalPassword 原始密码
     * @param newPassword      新密码
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "password", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePassword(Long uid, String originalPassword, String newPassword, String appid) throws Exception {
        App app = appService.getAppById(appid);

        authService.updatePassword(uid,
                AESUtil.decrypt(originalPassword, app.getKey()),
                AESUtil.decrypt(newPassword, app.getKey()));

        return ImmutableMap.of("result", true);
    }

}