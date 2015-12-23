package com.pemass.persist.enumeration;

/**
 * @Description: 积分类型
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:48
 */
public enum PointTypeEnum {
    /**
     * E积分
     */
    P("E积分"),

    /**
     * E通币
     */
    E("E通币"),

    /**
     * 一元购积分
     */
    O("一元购积分");

    private String description;

    PointTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
