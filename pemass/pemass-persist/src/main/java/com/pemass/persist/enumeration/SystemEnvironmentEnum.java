/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.enumeration;

/**
 * @Description: SystemEnvironmentEnum
 * @Author: estn.zuo
 * @CreateTime: 2015-09-18 14:47
 */
public enum SystemEnvironmentEnum {

    DEV("dev", "开发环境", "dev."),

    TEST("test", "测试环境", "dev."),

    PRE("pre", "预上线环境", "pre."),

    RELEASE("release", "", ""); //正式环境

    private String name;//name和POM.XML文件中一一对应

    private String description;//系统环境描述，主要用于显示当前系统是什么环境

    private String prefix;//域名前缀

    SystemEnvironmentEnum(String name, String description, String prefix) {
        this.name = name;
        this.description = description;
        this.prefix = prefix;
    }

    public static SystemEnvironmentEnum getByName(String name) {
        for (SystemEnvironmentEnum systemEnvironmentEnum : SystemEnvironmentEnum.values()) {
            if (systemEnvironmentEnum.getName().equals(name)) {
                return systemEnvironmentEnum;
            }
        }
        throw new IllegalArgumentException(name + "Is not in SystemEnvironmentEnum");
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }
}
