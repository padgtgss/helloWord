/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.enumeration;

/**
 * @Description: PresentPackEnum
 * @Author: oliver.he
 * @CreateTime: 2015-07-31 11:11
 */
public enum PresentPackTypeEnum {

    GENERAL("普通红包"),

    LUCK("拼手气红包");

    PresentPackTypeEnum(String description) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }
}