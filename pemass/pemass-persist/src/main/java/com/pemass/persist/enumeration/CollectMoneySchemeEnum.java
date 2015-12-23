package com.pemass.persist.enumeration;

/**
 * 收款送积分方案类型
 * Created by hejch on 2014/12/24.
 */
public enum CollectMoneySchemeEnum {


    SHOPPING_AMOUNT("按照购物金额赠送"),

    PERCENTAGE("按照百分比赠送"),

    IMMOBILIZATION("购物固定赠送");


    private String description;

    CollectMoneySchemeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
