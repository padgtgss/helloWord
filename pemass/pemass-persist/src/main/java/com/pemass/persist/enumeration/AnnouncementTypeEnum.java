/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: 公告分类枚举
 * @Author: estn.zuo
 * @CreateTime: 2014-12-30 19:44
 */
public enum AnnouncementTypeEnum {

    ANNOUNCEMENT("市场公告"),

    HELP("新手帮助"),

    AGREEMENT("协议说明"),

    FAQ("问题解析");

    private String description;

    AnnouncementTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
