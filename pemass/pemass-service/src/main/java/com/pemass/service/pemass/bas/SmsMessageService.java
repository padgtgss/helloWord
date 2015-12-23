/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas;

import com.pemass.persist.domain.jpa.sms.SmsMessage;
import com.pemass.persist.domain.vo.SmsMessageVo;
import com.pemass.persist.enumeration.SmsTypeEnum;

/**
 * @Description: SmsMessageService
 * @Author: zhou hang
 * @CreateTime: 2015-01-20 11:16
 */
public interface SmsMessageService {
    /**
     * 推送短信
     * @param smsMessageVo
     */
    SmsMessage insert(SmsMessageVo smsMessageVo);

    /**
     * 发送短信
     * @param smsMessageVo
     * @return
     */
    String smsSend(SmsMessageVo smsMessageVo);

    /**
     *
     * @param telephone
     * @param smsTypeEnum
     * @param objects
     */
    void append(String telephone, SmsTypeEnum smsTypeEnum, Object[] objects);

    /**
     * app端校验验证码
     * @param username
     * @param regVal
     * @param validateCode
     * @return
     */
    boolean validateCode(String username, SmsTypeEnum regVal, String validateCode);

    /**
     * app端校验验证码
     * @param username
     * @param customOrderNoreg
     * @param customOrderReged
     * @param validateCode
     * @return
     */
    boolean validateCode(String username, SmsTypeEnum customOrderNoreg, SmsTypeEnum customOrderReged, String validateCode);

    /**
     * web端校验验证码
     * @param username
     * @param customOrderNoreg
     * @param customOrderReged
     * @param validateCode
     * @return
     */
    boolean validateCodes(String username, SmsTypeEnum customOrderNoreg, SmsTypeEnum customOrderReged, String validateCode);

    /**
     * web端校验验证码
     * @param username
     * @param customOrderNoreg
     * @param validateCode
     * @return
     */
    boolean validateCodes(String username,SmsTypeEnum customOrderNoreg,String validateCode);

    /**
     * 更新
     *
     * @param smsMessage
     * @return
     */
    boolean update(SmsMessage smsMessage);
}
