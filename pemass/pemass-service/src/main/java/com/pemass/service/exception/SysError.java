/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.exception;

import com.pemass.common.core.exception.PemassError;

/**
 * @Description: SysError
 * @Author: zhou hang
 * @CreateTime: 2015-06-24 16:17
 */
public enum SysError implements PemassError {

    /*- 用户类错误 -*/
    USERNAME_HAS_EXISTENCE("0001", "用户名已存在"),
    PASSWORD_ERROR("0002", "密码错误"),
    USERNAME_NOT_EXIST("0003", "用户名不存在"),
    TERMINAlUSER_NOT_EXIST("0004", "收银员不存在"),
    VALIDATE_CODE_ERROR("0005", "验证码错误"),
    CASHIERUSERID_NOT_EXIST("0006", "收银员不存在"),
    LOGIN_TIMES_ERROR("0007", "登录失败次数过大"),
    USER_NOT_EXIST("0008", "用户不存在"),
    TICKETER_NOT_EXIST("0009", "检票员不存在"),
    USERID_IS_NOT_AUTHORITY("0010", "用户权限不足"),
    USER_IS_FREEZE("0011", "用户被冻结"),


    /*- 机构类错误 -*/
    ORGANIZATIONID_NOT_EXIST("1001", "机构不存在"),
    ORGANIZATIONID_IS_NOT_AUTHORITY("1002", "权限不足"),
    POS_HAS_BIND("1003", "该POS已经绑定"),
    POS_HAS_NOT_BIND("1004", "该POS未绑定"),

    /*-- 其他 --*/
    CONFIG_NOT_EXIST("9001","配置信息不存在"),
    USER_NOT_CREDIT("10005","用户还未授信"),
    USER_HAS_CREDIT("10005","请勿重复申请授信"),
    ;

    String errorCode;
    String errorMessage;
    private static final String ns = "SYS";

    SysError(String errorCode, String errorMessage) {
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