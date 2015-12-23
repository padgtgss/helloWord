/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.enumeration;

/**
 * @Description: 清算对象角色
 * <p/>
 * 参与清分对象
 * @Author: estn.zuo
 * @CreateTime: 2015-08-13 21:32
 */
public enum SettlementRoleEnum {

    /**
     * 积分通 ---> 商户
     */
    CONVERT_POINT_E("兑换E通币"),
    /**
     * 商户 ---> 积分通
     */
    PRODUCT_PRICE("平台手续费+分销本金"),
    /**
     * 积分通 ---> 商户
     */
    DISTRIBUTION_PROFIT("分销利润"),
    /**
     * 积分通 ---> 商户
     */
    PRODUCT_PRINCIPAL("商品本金"),
    /**
     * 商户 ---> 积分通
     */
    POINT_E_PRINCIPAL("E通币本金"),
    /**
     * 商户 ---> 积分通
     */
    POINT_P_CHARGE("E积分手续费"),
    /**
     * 积分通 ---> 积分通
     */
    CASHBACK("返现"),
    /**
     * 积分通 ---> 积分通
     */
    SYSTEM_CHARGE("平台服务费"),
    /**
     * 积分通 ---> 省公司
     */
    POINT_PROFIT_FOR_ISSUE("发分省公司服务费"),
    /**
     * 积分通 ---> 省公司
     */
    POINT_PROFIT_FOR_ACCEPT("收单省公司服务费"),
    /**
     * 积分通 ---> 代理商
     */
    AGENT_PROFIT_FOR_ISSUE("发分代理商利润"),
    /**
     * 积分通 ---> 代理商
     */
    AGENT_PROFIT_FOR_ACCEPT("收单代理商利润"),
    /**
     * 积分通 ---> 商户
     */
    POINT_FOR_ISSUE("发分商户利润"),

    /**
     * 商户 ---> 积分通
     */
    CALL_BACK_POINT_E("返E通币"),

    /**
     * 积分通 ---> 银联
     */
    SERVICE_PAY_PRICE("代付手续费"),

    /**
     * 积分通 ---> 银联
     */
    SERVICE_CUT_PRICE("代扣手续费"),

    /**
     * 商户 ---> 积分通
     */
    SERVICE_PAY_BACK_PRICE("返还代付手续费"),

    ;


    private String description;

    SettlementRoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
