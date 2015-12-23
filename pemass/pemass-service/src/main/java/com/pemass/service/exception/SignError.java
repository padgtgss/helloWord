package com.pemass.service.exception;


import com.pemass.common.core.exception.PemassError;

/**
 * @Description: 签名异常
 * @Author: estn.zuo
 * @CreateTime: 2014-10-31 10:29
 */
public enum SignError implements PemassError {

    TOKEN_NOT_FOUND("0001", "未找到访问令牌"),
    TOKEN_ERROR("0002", "访问令牌错误"),
    TOKEN_EXPIRY("0003", "访问令牌过期"),
    TOKEN_IS_NOT_ALLOW("0004","BALABALA, YOU ARE A BAD BOY"),

    SIGNATURE_ERROR("1001", "签名失败"),
    REPLAY_ERROR("1002", "重复调用"),
    STAMP_NOT_FOUND("1003", "未找到STAMP"),;

    String errorCode;
    String errorMessage;
    private static final String ns = "SIG";

    SignError(String errorCode, String errorMessage) {
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
