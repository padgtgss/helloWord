/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: LogisticsStatusEnum
 * @Author: zhou hang
 * @CreateTime: 2015-04-14 17:10
 */
public enum LogisticsStatusEnum {

    PUTAWAY("入库"),
    ALLOT("调拨");

    private String description;

    LogisticsStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static LogisticsStatusEnum getByDescription(String description) {
        for (LogisticsStatusEnum logisticsStatusEnum : LogisticsStatusEnum.values()) {
            if (logisticsStatusEnum.getDescription().equals(description)) {
                return logisticsStatusEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in TimingTaskEnum");
    }

}
