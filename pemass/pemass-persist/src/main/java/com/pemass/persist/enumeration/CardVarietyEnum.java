/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: CardVarietyEnum
 * @Author: zhou hang
 * @CreateTime: 2015-04-14 17:15
 */
public enum CardVarietyEnum {

    DISK_CARD("磁条卡"),
    CHIP_CARD("芯片卡");

    private String description;

    CardVarietyEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static CardVarietyEnum getByDescription(String description) {
        for (CardVarietyEnum cardVarietyEnum : CardVarietyEnum.values()) {
            if (cardVarietyEnum.getDescription().equals(description)) {
                return cardVarietyEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in TimingTaskEnum");
    }

}
