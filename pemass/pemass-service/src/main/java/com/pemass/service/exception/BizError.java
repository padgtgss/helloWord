/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.exception;

import com.pemass.common.core.exception.PemassError;

/**
 * @Description: BizError
 * @Author: zhou hang
 * @CreateTime: 2015-06-24 16:20
 */
public enum BizError implements PemassError {
    /**
     * 银通卡异常
     */
    CARDNUMBER_IS_NULL("0001", "卡号不能为空"),
    CARDCATEGORY_IS_NULL("0002", "卡号类型不能为空"),
    NOT_MATCHING("0003", "卡号和类型数量不匹配"),
    BEGINNUMBER_IS_NULL("0004", "起始卡号不能为空"),
    ENDNUMBER_IS_NULL("0005", "结束卡号不能为空"),
    CARDNUMBER_NOT_EXIST("0006", "卡号不存在"),
    CARD_IS_USED("0007", "号卡已使用"),
    CADR_NOT_PUTAWAY("0008", "此卡信息不正确 不能录入"),
    CARD_NOT_ALLOT("0009", "此卡暂时不能调拨"),
    DEPOSIT_NOT_CORRECT("0010","圈存手续费不正确"),
    MONEY_NOT_CORRECT("0011","圈存金额不正确"),
    POINT_E_AMOUNT_NOT_CORRECT("0012","圈存E通币数额不正确"),
    USER_NO_CARD("0013","用户未绑定银通卡"),
    USER_HAS_CARD("0014","用户已存在绑定的银通卡"),
    CARD_IS_BIND("0015","该卡号已被绑定"),
    CARD_NOT_BIND("0016","该卡号不能绑定"),
    /**
     * 商品异常
     */
    PRODUCT_NOT_MATCHING_SITE("1001", "营业点与产品不匹配"),
    PRODUCT_STOCK_NUMBER_IS_NOT_ENOUGH("1002", "该商品库存不足"),
    PRODUCT_IS_NOT_EXIST("1003", "该商品不存在"),;

    String errorCode;
    String errorMessage;
    private static final String ns = "BIZ";

    BizError(String errorCode, String errorMessage) {
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
