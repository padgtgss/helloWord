/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.enumeration;

/**
 * @Description: Sequence字段枚举值
 * @Author: estn.zuo
 * @CreateTime: 2015-07-10 00:37
 */
public enum SequenceEnum {

    /* 1:积分 */

    /**
     * 积分池
     */
    POINT_POOL(10),
    /**
     * 积分认购
     */
    POINT_PURCHASE(11),
    /**
     * 红包
     */
    PRESENT_PACK(12),
    /**
     * 积分充值券
     */
    POINT_COUPON(13),

    /* 2:商家/商品 */

    /**
     * 商品
     */
    PRODUCT(20),
    /**
     * 营业点
     */
    SITE(21),

    /**
     * 中银通卡
     */
    EXPRESSPAY_CARD(22),

    /**
     * 商家
     */
    ORGANIZATION(23),

    /**
     * 银通卡圈存
     */
    OPERATE(24),

    /* 8:订单 */

    /**
     * 订单编号
     */
    ORDER(80),
    /**
     * 自定义订单编号
     */
    CUSTOMIZATION_ORDER(81),
    /**
     * 订单支付流水号
     */
    PAYMENT(82),
    /**
     * 一元购订单
     */
    ONE_PURCHASE_ORDER(83),
    /**
     * 交易编号
     */
    TRANSACTION(84),

    /* 9:其他 */

    /**
     * 抽奖
     */
    LOTTERY_DRAW(90),;

    private int prefix; //编号前缀,不能重复

    SequenceEnum(int prefix) {
        this.prefix = prefix;
    }

    public int getPrefix() {
        return this.prefix;
    }

}
