package com.pemass.persist.enumeration;

/**
 * @Description: 职务角色
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:59
 */
public enum PositionRoleEnum {
    POSITION_ROLE_ADMIN("管理员"), POSITION_ROLE_FINANCE("财务"),POSITION_ROLE_BUSINESS("业务");

    private String description;

    PositionRoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public  static  PositionRoleEnum getByDescription(String description) {
        for (PositionRoleEnum positionRoleEnum : PositionRoleEnum.values())
            if (positionRoleEnum.getDescription().equals(description)) {
                return positionRoleEnum;
            }
        throw new IllegalArgumentException(description + "Is not in PositionRoleEnum");
    }

}
