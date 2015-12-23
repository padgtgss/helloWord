package com.pemass.persist.enumeration;

/**
 * 策略的操作类型
 * Created by hejch on 2014/12/1.
 */
public enum StrategyTypeEnum {

    SEND_POINT("送积分"),
    SEND_PRESENT("送红包"),
    SEND_ONE_PURCHASE("一元购积分策略"),
    SEND_PRESENT_SQUARE("放红包到广场"),
    CONTENT_PRESENT_NOSHOW("放红包到广场不展示"),
    UAV("无人机");

    private String description;

    StrategyTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static StrategyTypeEnum getByDescription(String description) {
        for (StrategyTypeEnum strategyTypeEnum : StrategyTypeEnum.values()) {
            if (strategyTypeEnum.getDescription().equals(description)) {
                return strategyTypeEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in StrategyTypeEnum");
    }

}
