/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: 新闻内容类型
 * @Author: estn.zuo
 * @CreateTime: 2014-11-16 11:15
 */
public enum NewsTypeEnum {
    /**
     * 云钱包的BANNER
     */
    CLOUD_MONEY_BANNER("云钱包BANNER"),

    @Deprecated
    COMMUNICATION("互动");

    private String description;

    NewsTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static NewsTypeEnum getByDescription(String description) {
        for (NewsTypeEnum newsTypeEnum : NewsTypeEnum.values()) {
            if (newsTypeEnum.getDescription().equals(description)) {
                return newsTypeEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in NewsTypeEnum");
    }

}
