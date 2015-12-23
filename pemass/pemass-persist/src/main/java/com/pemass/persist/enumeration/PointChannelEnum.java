package com.pemass.persist.enumeration;

/**
 * @Description: 积分来源
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:48
 */
public enum PointChannelEnum {
    /**
     * 0-红包
     */
    PRESENT("红包"),

    /**
     * 1-系统赠送
     */
    SYSTEM_LARGESS("系统赠送"),

    /**
     * 2-购买商品所得
     */
    PURCHASE_GOODS("购买商品所得"),

    /**
     * 3-通过营销策略商户直接赠送
     */
    STRATEGY_LARGESS("营销策略赠送"),

    /**
     * 4,-购买商品退还所得
     */
    PURCHASE_GOODS_RETURN("购买商品退还"),

    /**
     * 5,自定以赠送
     */
    CUSTOMIZATION("自定义赠送"),
    /**
     * 6,转盘抽奖
     */
    LUCKY_DRAW("转盘抽奖"),

    /**
     * 7,一元购购买商品获得积分
     */
    ONE_POINT_PURCHASE("购买一元购商品"),

    /**
     * 8,积分券充值
     */
    POINT_COUPON("积分券充值"),

    /**
     * 9,一元购积分朋友赠送
     */
    ONE_POINT_GIVE("朋友赠送"),

    /**
     * 10,商户认购
     */
    ORGANIZATION_SUBSCRIBE("商户认购"),

    /**
     * 11,一元购积分从朋友处回收
     */
    ONE_POINT_RECOVERY("朋友回收"),
    /**
     * 12,活动赠送
     */
    ACTIVITY_GIFT("活动赠送"),;

    private String description;

    PointChannelEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PointChannelEnum getByDescription(String description) {
        for (PointChannelEnum presentSourceEnum : PointChannelEnum.values()) {
            if (presentSourceEnum.getDescription().equals(description)) {
                return presentSourceEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in PointSourceEnum");
    }

    public static String getByNO(String No, String organizatioName) {

        if ("0".equals(No)) {
            return "拆红包";
        }
        if ("1".equals(No)) {
            return "系统赠送";
        }
        if ("2".equals(No)) {
            return "购买商品赠送";
        }
        if ("3".equals(No)) {
            if ("".equals(organizatioName)) {
                return "营销策略直接赠送";
            }
            return organizatioName + "直接赠送";
        }
        if ("4".equals(No)) {
            return "订单退返";
        }
        if ("5".equals(No)) {
            return "自定义赠送";
        }
        if ("6".equals(No)) {
            return "转盘抽奖";
        }
        if ("7".equals(No)) {
            return "一元购赠送";
        }
        if ("8".equals(No)) {
            return "积分券充值";
        }

        if ("9".equals(No)) {
            return "朋友赠送";
        }
        if ("11".equals(No)) {
            return "朋友回收";
        }
        return "系统赠送";
    }
}
