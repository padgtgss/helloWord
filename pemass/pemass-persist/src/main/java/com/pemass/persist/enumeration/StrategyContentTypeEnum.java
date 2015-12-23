/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: StrategyContentType
 * @Author: he jun cheng
 * @CreateTime: 2015-05-05 14:50
 */
public enum StrategyContentTypeEnum {

    POINT("积分"),
    PRESENT("红包");

    private String description;

    StrategyContentTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}