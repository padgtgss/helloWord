package com.pemass.cloudmoney.api.controller.sys;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.util.FileUtil;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.EmailVo;
import com.pemass.persist.domain.vo.SmsMessageVo;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.pojo.sys.UserPojo;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.SysError;
import com.pemass.service.jms.Producer;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.email.SendmailService;
import com.pemass.service.pemass.sys.UserService;
import org.joda.time.DateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 用户信息
 * Author: estn.zuo
 * CreateTime: 2014-07-12 14:43
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Resource
    public UserService userService;

    @Resource
    public SendmailService sendmailService;

    @Resource
    private Producer mailProducer;

    @Resource
    private SmsMessageService smsMessageService;

    /**
     * 更新用户信息
     * <p/>
     * 不能更新一些敏感字段
     *
     * @param user
     * @param avatarFile 头像
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{uid}", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public Object update(@PathVariable("uid") Long uid, User user, @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) throws Exception {
        if (avatarFile != null && !avatarFile.isEmpty() && avatarFile.getSize() != 0) {
            String avatarPath = FileUtil.saveFile(avatarFile.getInputStream(), FileUtil.fetchExtension(avatarFile.getOriginalFilename()));
            user.setAvatar(avatarPath);
        }
        user.setId(uid);
        userService.update(user);
        return ImmutableMap.of("result", true);
    }

    /**
     * 获取用户详情
     *
     * @param uid
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@PathVariable("uid") Long uid) {
        User user = new User();
        user.setId(uid);
        user = userService.getById(uid);

        UserPojo userPojo = new UserPojo();
        MergeUtil.merge(user, userPojo);
        return userPojo;
    }

    /**
     * 注册时发送验证码<p>
     * 并将验证码存储在内存
     *
     * @param email 邮箱
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "email/validate", method = RequestMethod.POST)
    @ResponseBody
    public Object emailValidate(Long uid, String email) {

        String validateCode = UUIDUtil.randomNumber(6);
        User user = userService.getById(uid);

        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("username", user.getUsername());
        paraMap.put("validateCode", validateCode);
        paraMap.put("createTime", DateTime.now().toString(SystemConst.DATE_FORMAT));

        EmailVo emailVo = new EmailVo();
        emailVo.setTo(email);
        emailVo.setSubject("PEMASS绑定邮箱");
        emailVo.setTemplate("userRegister.html");
        emailVo.setParaMap(paraMap);

        mailProducer.send(emailVo);

        PemassConst.VALIDATE_CODE.put("USER_EMAIL" + email, validateCode);

        SmsMessageVo smsMessageVo = new SmsMessageVo();
        smsMessageVo.setReceiveNumber(email);
        smsMessageVo.setSmsType(SmsTypeEnum.MAIL_USER_BIND);
        List<Object> validatecodeList = new ArrayList<Object>();
        validatecodeList.add(validateCode);
        smsMessageVo.setList(validatecodeList);


        smsMessageService.insert(smsMessageVo);

        return ImmutableMap.of("result", true);
    }

    /**
     * 绑定用户邮箱
     *
     * @param uid
     * @param email
     * @param validateCode
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "email", method = RequestMethod.POST)
    @ResponseBody
    public Object bindEmail(Long uid, String email, String validateCode) throws Exception {
        /*-- 验证验证码 --*/
        boolean isValidateSuccess = smsMessageService.validateCode(email, SmsTypeEnum.MAIL_USER_BIND, validateCode);
        if(!isValidateSuccess) {
            throw new BaseBusinessException(SysError.VALIDATE_CODE_ERROR);
        }

        User user = new User();
        user.setId(uid);
        user.setEmail(email);
        userService.update(user);

        return ImmutableMap.of("result", true);
    }


}