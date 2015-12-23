/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.bas;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.service.exception.SmsError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.bas.SmsMessageService;

import com.pemass.service.pemass.bas.SmsMessageValidateService;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: 验证码
 * @Author: estn.zuo
 * @CreateTime: 2015-04-16 10:32
 */
@Controller
@RequestMapping("/validate")
public class ValidateController {

    @Resource
    public UserService userService;

    @Resource
    private SmsMessageValidateService smsMessageValidateService;

    @Resource
    private SmsMessageService smsMessageService;

    /**
     * 用户注册时，发送验证码
     *
     * @param telephone
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "smsType=REG_VAL")
    @ResponseBody
    public Object validate4Register(String telephone) {

         /*-- 判断用户名是否存在 --*/
        if (userService.getByUsername(telephone) != null) {
            throw new BaseBusinessException(SysError.USERNAME_HAS_EXISTENCE);
        }

        if (smsMessageValidateService.validateSms(telephone) == false){
            throw new BaseBusinessException(SmsError.SMS_VALIDATE_ERROR);
        }

        smsMessageService.append(telephone, SmsTypeEnum.REG_VAL, null);
        return ImmutableMap.of("result", true);
    }

    /**
     * 用户找回密码时，发送验证码
     *
     * @param telephone
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "smsType=FIND_PWD")
    @ResponseBody
    public Object validate4FindPassword(String telephone) {
        /** 验证用户是否存在 */
        User user = userService.getByUsername(telephone);
        if (user == null) {
            throw new BaseBusinessException(SysError.USERNAME_NOT_EXIST);
        }
        /** 发送验证码 */

        smsMessageService.append(telephone, SmsTypeEnum.FIND_PWD, null);
        return ImmutableMap.of("result", true);
    }

    @RequestMapping(method = RequestMethod.POST,value = "testValidate4FindPassword1")
    @ResponseBody
    public Object validate4FindPassword1(String telephone,String key,String [] arg) {
//        smsService.sendSms(telephone,key,arg);
        return ImmutableMap.of("result", true);
    }

}
