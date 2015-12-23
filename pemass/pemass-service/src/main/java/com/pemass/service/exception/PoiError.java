/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.exception;

import com.pemass.common.core.exception.PemassError;

/**
 * @Description: PoiError
 * @Author: zhou hang
 * @CreateTime: 2015-06-24 16:25
 */
public enum PoiError implements PemassError {

    POINT_COUPON_NOT_EXIST("0001", "充值券不存在"),
    POINT_DETAILID_NOT_EXIST("0002", "积分明细不存在"),

    POINT_E_NOT_ENOUGH("0003", "E通币不足"),
    POINT_P_NOT_ENOUGH("0004", "E积分不足"),

    INTEGRAL_NOT_USE("0005", "积分不够赠送"),

    COLLECTMONEYSTRATEGYID_NOT_EXIST("0010", "策略类型不存在"),
    CONSUMPTIONAMOUNT_IS_NULL("0011", "收款金额不能为空"),
    ONEPOINTDETAIL_IS_NULL("0012", "该商品不能还款"),
    ONEPOINTDETAIL_IS_LACKING("0013", "积分不足"),
    ONEPOINTDETAIL_CALLBACK_FAILURE("0014", "回收失败"),
    ONEPOINTDETAIL_NOT_FOUND("0015", "积分详情不存在"),
    ONEPOINTDETAIL_IS_FULL("0016", "返还积分超出总数"),
    USER_IS_NO_GIVE("0017", "不能自己赠送给自己"),
    LOTTERYDRAW_PRIZE_IS_NULL("0018", "奖项不能为空"),
    ISSUE_TO_USER_POINT_INSUFFICIENT("0019", "指定账号送积分，积分余额不足。"),
    ISSUE_TO_USER_PRESENT_INSUFFICIENT("0020", "指定账号送红包，红包余额不足。"),
    ISSUE_TO_SQUARE_PRESENT_INSUFFICIENT("0021", "将红包放到红包广场，红包余额不足。"),
    AMOUNT_NOT_ENOUGH("0023", "壹购积分不足,扣除失败"),
    /**
     * 策略
     */
    STRATEGY_NOT_EXIST("1001", "策略不存在"),
    STRATEGY_FORMAT_ERROR("1002", "策略格式错误"),

    /**
     * 红包
     */
    PRESENT_NOT_EXIST("2001", "红包不存在"),
    PRESENTNAME_IS_NULL("2002", "红包名称为空"),
    PRESENT_HAS_GRAB_FINISH("2003", "红包已抢完"),
    PRESENT_SHOULD_NOT_GRANT("2004", "红包不能被赠送"),
    PRESENT_SHOULD_NOT_PACK("2005", "红包不能拆开"),
    PRESENT_HAS_ALREADY_GRAB("2006", "红包已抢"),;

    String errorCode;
    String errorMessage;
    private static final String ns = "POI";

    PoiError(String errorCode, String errorMessage) {
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
