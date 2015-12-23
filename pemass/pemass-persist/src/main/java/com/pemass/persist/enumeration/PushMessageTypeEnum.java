package com.pemass.persist.enumeration;

public enum PushMessageTypeEnum {

        FREEZE_POINT("提交订单冻结积分"),

        DEDUCTION_POINT("支付成功扣除积分"),

        GRANT_PRESENT("朋友给您送红包咯~"),

        GRANT_PRESENT_FROM_ORGANIZATION("商家福利大派送"),

        GRANT_POINT_FROM_ORGANIZATION("商家福利大派送"),

        GRANT_POINT_FROM_REGISTER("注册成功，送积分咯~"),

        AUDIT_SUCCESS("授信成功咯~"),

        AUDIT_FAIL("授信成功"),

        AUDIT_EXPIRY("授信额度到期提醒"),

        TICKET_HAS_CHECKED("电子票已成功使用"),

        SYSTEM_PUSH("系统推送"),

        SEND_DIRECTION("直接发送"),
        ;

        private String title;

        PushMessageTypeEnum(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

    }