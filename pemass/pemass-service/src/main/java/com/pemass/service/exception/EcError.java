/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.exception;

import com.pemass.common.core.exception.PemassError;

/**
 * @Description: EcError
 * @Author: zhou hang
 * @CreateTime: 2015-06-24 16:18
 */
public enum EcError implements PemassError {
    /**
     * 订单异常
     */
    ORDER_NOT_FOUND("0001", "未找到订单"),
    ORDER_HAS_NOT_CONFIRMED("0002", "订单未确认"),
    ORDER_HAS_NOT_PAY("0003", "订单未支付"),
    ORDER_EXPIRY("0004", "订单已过期"),
    ORDER_HAS_NOT_ITEM("0005", "订单无订单项"),
    ORDER_ITEM_IS_NOT_GIVING("0006", "该商品不能够赠送"),
    ORDER_IDENTIFIER_NOT_EXIST("0007", "订单编号不存在"),
    ORDER_ITEM("0008", "订单无订单项"),
    ORDER_CODE_EXPIRY("0009", "订单码已过期"),

    /**
     * 自定义订单异常
     */
    CUSTOMIZATION_ORDER_IS_NULL("1001", "自定义订单对象为空"),

    /**
     * 电子票异常
     */
    CAN_NOT_CHECK_TICKET("2001", "不可检当前票"),
    TICKET_HAS_USED("2002", "电子票已使用"),
    TICKET_NOT_FOUND("2003", "电子票不存在"),

    /**
     * 支付异常
     */
    ORDER_HAS_PAY("3001", "订单已支付"),
    PAYMENT_TOTAL_PRICE_ERROR("3002", "订单支付金额不正确"),
    PAY_CODE_EXPIRY("0008", "付款码已过期"),



    /**
     * 业务异常 不可查看其他营业点的订单
     */
    CAN_NOT_SEARCH("4001","不可查看其他营业点的订单"),
    ;


    String errorCode;
    String errorMessage;
    private static final String ns = "EC";

    EcError(String errorCode, String errorMessage) {
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