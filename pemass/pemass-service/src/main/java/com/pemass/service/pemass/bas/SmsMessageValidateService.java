/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas;

/**
 * @Description: SmsMessageService
 * @Author: zhou hang
 * @CreateTime: 2015-01-20 11:16
 */
public interface SmsMessageValidateService {

    /**
     * 验证是否可向该号码发送短信
     *
     * @param telephone
     * @return
     */
    boolean validateSms(String telephone);



}
