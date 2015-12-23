package com.pemass.persist.enumeration;


/**
 * @Description: 账户角色
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:48
 */
public enum AccountRoleEnum {

    @Deprecated
    ROLE_LANDSCAPE("景点/景区及管理公司", 0),
    ROLE_SUPPLIER("商户", 1),
    @Deprecated
    ROLE_PRIMARY_DISTRIBUTION("代理分销商户", 2),
    ROLE_SECOND_DISTRIBUTION("二级分销商", 3),
    @Deprecated
    ROLE_OTHER("个人或个体商户、其他类型商户", 4),
    ROLE_CHANNEL("代理商", 5);

    private String description;
    private int ordinalReplace;

    AccountRoleEnum(String description, int ordinalReplace) {
        this.description = description;
        this.ordinalReplace = ordinalReplace;
    }

    public String getDescription() {
        return description;
    }

    public int getOrdinalReplace() {
        return ordinalReplace;
    }

    public void setOrdinalReplace(int ordinalReplace) {
        this.ordinalReplace = ordinalReplace;
    }

    public static AccountRoleEnum getByDescription(String description) {
        for (AccountRoleEnum accountRoleEnum : AccountRoleEnum.values()) {
            if (accountRoleEnum.getDescription().equals(description)) {
                return accountRoleEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in AccountRoleEnum");
    }


}
