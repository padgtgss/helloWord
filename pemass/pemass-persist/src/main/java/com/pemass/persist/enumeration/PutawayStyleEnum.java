/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: PutawayStyleEnum
 * @Author: zhou hang
 * @CreateTime: 2015-05-15 16:05
 */
public enum PutawayStyleEnum {
    BATCH_QUANTITY("批量"),
    SCAN("扫描");

    private String description;

    PutawayStyleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PutawayStyleEnum getByDescription(String description) {
        for (PutawayStyleEnum putawayStyleEnum : PutawayStyleEnum.values()) {
            if (putawayStyleEnum.getDescription().equals(description)) {
                return putawayStyleEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in TimingTaskEnum");
    }
}
