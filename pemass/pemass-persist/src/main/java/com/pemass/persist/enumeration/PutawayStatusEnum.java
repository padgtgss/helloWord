/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: PutawayStatusEnum
 * @Author: zhou hang
 * @CreateTime: 2015-05-13 17:43
 */
public enum PutawayStatusEnum {

    INTRANSIT("在途"),
    PUTAWAY("入库");

    private String description;

    PutawayStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PutawayStatusEnum getByDescription(String description) {
        for (PutawayStatusEnum putawayStatusEnum : PutawayStatusEnum.values()) {
            if (putawayStatusEnum.getDescription().equals(description)) {
                return putawayStatusEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in TimingTaskEnum");
    }
}
