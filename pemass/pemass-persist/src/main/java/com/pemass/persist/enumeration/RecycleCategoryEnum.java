package com.pemass.persist.enumeration;

/**
 * @author luoc
 * @Description:回收类别
 * @date 2015/7/10
 */
public enum RecycleCategoryEnum {
    /**
     * 0-红包
     */
    PRESENT("红包"),

    /**
     * 1-积分
     */
    POINT("积分");

    private String description;

    RecycleCategoryEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public  static  RecycleCategoryEnum getByDescription(String description) {
        for (RecycleCategoryEnum recycleCategoryEnum : RecycleCategoryEnum.values()) {
            if (recycleCategoryEnum.getDescription().equals(description)) {
                return recycleCategoryEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in RecycleCategoryEnum");
    }

}
