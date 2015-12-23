package com.pemass.cloudpos.api.controller.sys;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.redis.App;
import com.pemass.persist.domain.redis.Token;
import com.pemass.pojo.sys.TerminalUserPojo;
import com.pemass.pojo.sys.TokenPojo;
import com.pemass.service.pemass.sys.AppService;
import com.pemass.service.pemass.sys.OrganizationService;
import com.pemass.service.pemass.sys.TerminalUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private Log logger = LogFactory.getLog(AuthController.class);

    @Resource
    TerminalUserService terminalUserService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    AppService appService;

    /**
     * B端登陆
     *
     * @param terminalUsername 用户名
     * @param password         密码
     * @param deviceId         设备ID
     * @param appid            appid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Object terminalLogin(String terminalUsername, String password, Long deviceId, String appid) throws Exception {

        if (logger.isDebugEnabled()) {
            logger.debug("[terminalUsername:" + terminalUsername + ",password:" + password + ",deviceId:" + deviceId + "]");
        }

        App app = appService.getAppById(appid);

        Token token = terminalUserService.terminalLogin(terminalUsername, AESUtil.decrypt(password, app.getKey()), deviceId);

        TerminalUser terminalUser = terminalUserService.retrieveUserByUUID(token.getTargetUUID());

        Organization organization = organizationService.getOrganizationById(terminalUser.getOrganizationId());

        /**封装返回数据*/
        TokenPojo tokenPojo = new TokenPojo();
        TerminalUserPojo terminalUserPojo = new TerminalUserPojo();

        tokenPojo.setToken(token.getToken());
        terminalUserPojo.setOrganizationName(organization.getOrganizationName());
        terminalUserPojo.setOnePayAuditStatus(organization.getOnePayAuditStatus());

        MergeUtil.merge(terminalUser, terminalUserPojo);
        tokenPojo.setTerminalUser(terminalUserPojo);

        return tokenPojo;
    }

    /**
     * 修改密码
     *
     * @param terminalUserId   B端用户id
     * @param originalPassword 原始密码
     * @param newPassword      新密码
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_CASHIER','ROLE_TICKETER')")
    @RequestMapping(value = "password", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePassword(Long terminalUserId, String originalPassword, String newPassword, String appid) throws Exception {
        App app = appService.getAppById(appid);
        Boolean result  = terminalUserService.updatePassword(terminalUserId,
                AESUtil.decrypt(originalPassword, app.getKey()),
                AESUtil.decrypt(newPassword, app.getKey()));
        return ImmutableMap.of("result",result);
    }


}