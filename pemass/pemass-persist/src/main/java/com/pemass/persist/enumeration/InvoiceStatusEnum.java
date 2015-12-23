package com.pemass.persist.enumeration;

/**
 * Created by Administrator on 2014/12/10.
 */
public enum InvoiceStatusEnum {

    NONE_SEND("未寄出"),

    HAS_SEND("已寄出");

    private String description;

    InvoiceStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static InvoiceStatusEnum getByDescription(String description) {
        for (InvoiceStatusEnum billStatusEnum : InvoiceStatusEnum.values()) {
            if (billStatusEnum.getDescription().equals(description)) {
                return billStatusEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in InvoiceStatusEnum");
    }

}
