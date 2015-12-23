package com.pemass.persist.enumeration;


/**
 * @Description: 短信验证码类型
 * @Author: zhou hang
 * @CreateTime: 2015-01-20 09:54
 */
public enum SmsTypeEnum {

    /**
     * 短信类
     */
    ORDER_VAL_REGED("订单校验（已注册）"),
    ORDER_VAL_NOREG("订单校验（未注册）"),
    ORDER_SUCCESS("订购成功"),
    ORDER_SUCCESS_AF("订购成功后续"),
    ORDER_RETURN("退货短信"),
    CUSTOM_ORDER_REGED("自定义支付订单校验（已注册）"),
    CUSTOM_ORDER_NOREG("自定义支付订单校验（未注册）"),
    CUSTOM_ORDER_PAY("自定义付款成功"),

    REG_VAL("用户注册"),
    FIND_PWD("找回密码"),
    PRESENT_REGED("好友红包（已注册）"),
    PRESENT_NOREG("好友红包（未注册）"),

    REMOVE_TEL("解绑"),
    SET_TEL("绑定"),

    SYS_REG("系统创建用户"),

    SECOND_AUDIT_FAILURE("二次审核失败"),
    PROVIDER_AUDIT_SUCCESS("代理商添加的商户审核通过"),
    REG_AUDIT_SUCCESS("注册并由积分通运营人员审核通过"),
    REG_AUDIT_FAILURE("注册并由积分通运营人员审核驳回"),
    ONE_POINT_AUDIT_SUCCESS("申请成为返现商户审核通过"),
    ONE_POINT_AUDIT_FAILURE("申请成为返现商户审核驳回"),
    ONE_POINT_PAY_AUDIT_SUCCESS("申请壹元购支付审核通过"),
    ONE_POINT_PAY_AUDIT_FAILURE("申请壹元购支付审核驳回"),
    /**
     * 邮件类
     */
    MAIL_REG("邮箱注册"),
    MAIL_RETRIEVE_PASSWORD("找回密码"),
    MAIL_ORGANIZATION_AUDIT_PASS("商户审核通过"),
    MAIL_ORGANIZATION_AUDIT_FAIL("商户审核驳回"),
    MAIL_SECOND_AUDIT_FAIL("商户二次审核驳回"),
    MAIL_YI_AUDIT_FAIL("一元购积分受理审核驳回"),
    MAIL_YI_AUDIT_PASS("一元购积分受理审核通过"),
    MAIL_USER_BIND("用户绑定邮箱"),
    MAIL_AGENTS_INPUT_SUCCESS("代理商录入资料审核成功"),
    MAIL_ONEPAY_AUDIT_PASS("申请壹元购支付审核通过"),
    MAIL_ONEPAY_AUDIT_FAIL("申请壹元购支付审核失败");


    private String description;

    SmsTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static SmsTypeEnum getByDescription(String description) {
        for (SmsTypeEnum smsTypeEnum : SmsTypeEnum.values()) {
            if (smsTypeEnum.getDescription().equals(description)) {
                return smsTypeEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in SmsTypeEnum");
    }
}
