package com.pemass.persist.enumeration;

/**
 * @Description: 审核状态
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:59
 */
public enum AuditStatusEnum {

    NONE_AUDIT("未审核"), ING_AUDIT("审核中"), HAS_AUDIT("已审核"), FAIL_AUDIT("审核失败");

    private String description;

    AuditStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
