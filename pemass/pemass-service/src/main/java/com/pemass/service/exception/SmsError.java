/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.exception;

import com.pemass.common.core.exception.PemassError;

/**
 * @Description: SmsMessageError
 * @Author: zhou hang
 * @CreateTime: 2015-01-21 14:27
 */
public enum SmsError implements PemassError {

    SMSMESSAGE_IS_NULL("0001","发送信息为空"),
    SMSMESSAGE_SMSTYPE_IS_NULL("0002","发送类型为空"),
    SMSMESSAGE_RECEIVENUMBER_IS_NULL("0003","接受人为空"),
    SMSMESSAGE_EXPIRYTIME_IS_NULL("0004","过期时间为空"),
    SMSMESSAGE_SEND_FAILURE("0005","短信发送失败"),
    SMS_ERROR("0006","短信发送失败"),
    VERIFICATION_ERROR("0007","验证码错误"),
    SMS_VALIDATE_ERROR("0008","一天之内同一号码接收注册短信不可超过三条")
    ;

    String errorCode;
    String errorMessage;
    private static final String ns = "SMS";

    SmsError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getNamespace() {
        return ns.toUpperCase();
    }

    public String getErrorCode() {
        return ns + "." + errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
