/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: ConsumeTypeEnum
 * @Author: estn.zuo
 * @CreateTime: 2014-12-03 17:42
 */
public enum ConsumeTypeEnum {
    /**
     * 订单
     * <p/>
     * ConsumeDetail.consumeTargetId记录:订单ID(Order.id)
     */
    ORDER("商品订单抵扣"),
    /**
     * 红包
     * <p/>
     * ConsumeDetail.consumeTargetId记录:红包ID(Present.id)
     */
    PRESENT("包红包抵扣"),
    /**
     * 自定义
     * <p/>
     * ConsumeDetail.consumeTargetId记录:自定义订单ID(Order.id)
     */
    CUSTOMIZATION("到店订单抵扣"),
    /**
     * 积分过期回收
     * <p/>
     * ConsumeDetail.consumeTargetId记录:积分池ID(PointPool.id)
     */
    INTEGRAL_OVERDUE_RECOVERY("积分过期回收"),
    /**
     * 营销策略赠送
     * <p/>
     * ConsumeDetail.consumeTargetId记录:策略ID(IssueStrategy.id)
     */
    STRATEGY("营销策略"),
    /**
     * 转盘抽奖
     * <p/>
     * ConsumeDetail.consumeTargetId记录:抽奖ID(LotteryDraw.id)
     */
    LUCKY_DRAW("转盘抽奖"),

    /**
     * 积分券
     * <p/>
     * ConsumeDetail.consumeTargetId记录:积分券ID(PointCoupon.id)
     */
    POINT_COUPON("积分券"),

    /**
     * 购买商品赠送
     * <p/>
     * ConsumeDetail.consumeTargetId记录:用户积分明细ID(UserPointDetail.id)
     */
    PURCHASE_GOODS_GIFT("购买商品赠送"),


    /**
     * 银通卡圈存
     * <p/>
     * ConsumeDetail.consumeTargetId记录:银通卡圈存明细明细ID(UserExpresspayCardDetail.id)
     */
    EXPRESSPAYCARD_DEPOSIT("银通卡圈存"),


    /**
     * 活动赠送
     * <p/>
     * ConsumeDetail.consumeTargetId记录:用户积分明细ID(UserPointDetail.id)
     */
    ACTIVITY_GIFT("活动赠送"),

    ;


    private String description;

    ConsumeTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ConsumeTypeEnum getByDescription(String description) {
        for (ConsumeTypeEnum consumeTypeEnum : ConsumeTypeEnum.values()) {
            if (consumeTypeEnum.getDescription().equals(description)) {
                return consumeTypeEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in ConsumeTypeEnum");
    }

    public static String getByNO(String No) {

        if ("ORDER".equals(No)) {
            return "购买商品抵扣";
        }
        if ("PRESENT".equals(No)) {
            return "包红包抵扣";
        }
        if ("CUSTOMIZATION".equals(No)) {
            return "自定义抵扣";
        }
        if ("INTEGRAL_OVERDUE_RECOVERY".equals(No)) {
            return "积分过期回收";
        }
        if ("LUCKY_DRAW".equals(No)) {
            return "转盘抽奖抵扣";
        }
        if ("POINT_COUPON".equals(No)) {
            return "积分券抵扣";
        }
        return "支出";
    }

}
